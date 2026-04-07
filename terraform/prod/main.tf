terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "5.81.0"
    }
  }
}

# --- 1. SECURITY (Network & IAM) ---

# Security Group for app (ECS Fargate)
resource "aws_security_group" "app_sg" {
  name        = "nexuspay-app-sg"
  description = "Allow app requests"

  ingress {
    from_port   = 8080
    to_port     = 8080
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

# Security Group for DB (RDS)
resource "aws_security_group" "db_sg" {
  name        = "nexuspay-db-sg"
  description = "Only app requests"

  ingress {
    from_port       = 5432
    to_port         = 5432
    protocol        = "tcp"
    security_groups = [aws_security_group.app_sg.id]
  }
}

# Lambda role (E-mail consumer)
resource "aws_iam_role" "lambda_exec_role" {
  name = "nexuspay-lambda-role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [{
      Action = "sts:AssumeRole"
      Effect = "Allow"
      Principal = { Service = "lambda.amazonaws.com" }
    }]
  })
}

# Lambda permissions: Logs, SQS e SES
resource "aws_iam_role_policy" "lambda_policy" {
  name = "nexuspay-lambda-policy"
  role = aws_iam_role.lambda_exec_role.id

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = ["sqs:ReceiveMessage", "sqs:DeleteMessage", "sqs:GetQueueAttributes"],
        Effect = "Allow",
        Resource = aws_sqs_queue.user_registration.arn
      },
      {
        Action = ["ses:SendEmail", "ses:SendRawEmail"],
        Effect = "Allow",
        Resource = "*"
      },
      {
        Action = ["logs:CreateLogGroup", "logs:CreateLogStream", "logs:PutLogEvents"],
        Effect = "Allow",
        Resource = "*"
      }
    ]
  })
}

# ECS execution role
resource "aws_iam_role" "ecs_execution_role" {
  name = "nexuspay-ecs-execution-role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [{
      Action = "sts:AssumeRole"
      Effect = "Allow"
      Principal = { Service = "ecs-tasks.amazonaws.com" }
    }]
  })
}

resource "aws_iam_role_policy_attachment" "ecs_execution_standard" {
  role       = aws_iam_role.ecs_execution_role.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy"
}

# --- 2. Message and DB recurses  ---

resource "aws_sqs_queue" "user_registration" {
  name                      = var.sqs_queue_name
  message_retention_seconds = 86400
}

resource "aws_db_instance" "nexuspay_db" {
  allocated_storage      = 20
  engine                 = "postgres"
  engine_version         = "16"
  instance_class         = "db.t3.micro"
  db_name                = "nexuspay_db"
  username               = "nexus_admin"
  password               = var.db_password
  vpc_security_group_ids = [aws_security_group.db_sg.id]
  skip_final_snapshot    = true
  publicly_accessible    = false
}

# --- 3. Compute (Lambda) ---

resource "aws_lambda_function" "email_worker" {
  function_name    = "nexuspay-email-consumer"
  handler          = "consumer_job.lambda_handler"
  runtime          = "python3.11"
  role             = aws_iam_role.lambda_exec_role.arn
  filename         = "consumer_job.zip"
  source_code_hash = fileexists("consumer_job.zip") ? filebase64sha256("consumer_job.zip") : null
}

resource "aws_lambda_event_source_mapping" "sqs_trigger" {
  event_source_arn = aws_sqs_queue.user_registration.arn
  function_name    = aws_lambda_function.email_worker.arn
  batch_size       = 5
}

resource "aws_ecs_cluster" "nexuspay_cluster" {
  name = "nexuspay-cluster"
}

resource "aws_ecs_task_definition" "nexuspay_task" {
  family                   = "nexuspay-task"
  network_mode             = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  cpu                      = "256"
  memory                   = "1024"
  execution_role_arn       = aws_iam_role.ecs_execution_role.arn

  container_definitions = jsonencode([
    {
      name  = "nexuspay-app"
      image = "seu-usuario/nexuspay:latest"
      portMappings = [{ containerPort = 8080, hostPort = 8080 }]
      environment = [
        { name = "DB_URL", value = "jdbc:postgresql://${aws_db_instance.nexuspay_db.endpoint}/nexuspay_db" },
        { name = "DB_USER", value = "nexus_admin" },
        { name = "DB_PASSWORD", value = var.db_password },
        { name = "SQS_QUEUE_URL", value = aws_sqs_queue.user_registration.id }
      ]
    }
  ])
}

resource "aws_ecs_service" "nexuspay_service" {
  name            = "nexuspay-service"
  cluster         = aws_ecs_cluster.nexuspay_cluster.id
  task_definition = aws_ecs_task_definition.nexuspay_task.arn
  desired_count   = 1
  launch_type     = "FARGATE"

  network_configuration {
    subnets          = ["subnet-xxxxxx", "subnet-yyyyyy"]
    security_groups  = [aws_security_group.app_sg.id]
    assign_public_ip = true
  }
}

resource "aws_ecr_repository" "nexuspay_repo" {
  name = "nexuspay-app-repo"
}