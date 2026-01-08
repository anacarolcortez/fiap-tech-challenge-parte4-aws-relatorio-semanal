resource "aws_cloudwatch_event_rule" "weekly_event" {
  name                = "event-relatorio-sns-weekly"
  schedule_expression = "cron(0 3 ? * SUN *)"
}

### Target do EventBridge → Lambda
resource "aws_cloudwatch_event_target" "lambda_target" {
  rule      = aws_cloudwatch_event_rule.weekly_event.name
  target_id = "event-relatorio-sns"
  arn       = aws_lambda_function.event_relatorio_sns.arn
}

### Permissão para o EventBridge invocar a Lambda
resource "aws_lambda_permission" "allow_eventbridge" {
  statement_id  = "AllowExecutionFromEventBridge"
  action        = "lambda:InvokeFunction"
  function_name = aws_lambda_function.event_relatorio_sns.function_name
  principal     = "events.amazonaws.com"
  source_arn    = aws_cloudwatch_event_rule.weekly_event.arn
}
