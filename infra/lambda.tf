resource "aws_lambda_function" "event_relatorio_sns" {
  function_name = "event-relatorio-sns"
  role          = aws_iam_role.lambda_exec_role.arn
  filename = "${path.module}/build/lambda/function.zip"
  handler  = "io.quarkus.amazon.lambda.runtime.QuarkusStreamHandler::handleRequest"
  runtime  = "java21"
  memory_size  = 512
  timeout      = 90

  vpc_config {
    subnet_ids         = var.vpc_subnet_ids
    security_group_ids = [var.lambda_security_group_id]
  }

  environment {
    variables = {
      SNS_TOPIC_ARN       = aws_sns_topic.relatorio_feedbacks.arn
      TIME_WINDOW_MINUTES = "15"
      DB_HOST             = "items-db.cin2aaa8eq2l.us-east-1.rds.amazonaws.com"
      DB_NAME             = var.db_name
      DB_USER             = var.db_username
      DB_PASS             = var.db_password
    }
  }
}