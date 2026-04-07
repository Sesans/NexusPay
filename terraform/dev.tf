provider "aws" {
  region                      = "sa-east-1"
  access_key                  = "test"
  secret_key                  = "test"
  skip_credentials_validation = true
  skip_metadata_api_check     = true
  skip_requesting_account_id  = true

  endpoints {
    sqs    = "http://localhost:4566"
    lambda = "http://localhost:4566"
    iam    = "http://localhost:4566"
    ses    = "http://localhost:4566"
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

# --- 2. Message and DB recurses  ---

resource "aws_sqs_queue" "user_registration" {
  name                      = var.sqs_queue_name
  message_retention_seconds = 86400
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