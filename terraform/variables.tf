variable "aws_region" {
  default = "sa-east-1"
}

variable "db_password" {
  description = "Password from .env or Secrets Manager"
  sensitive   = true
}

variable "sqs_queue_name" {
  default = "nexuspay-user-registered-queue"
}