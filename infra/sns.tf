resource "aws_sns_topic" "relatorio_feedbacks" {
  name = "relatorio_feedbacks-sns"
}


resource "aws_sns_topic_subscription" "feedback_email" {
  topic_arn = aws_sns_topic.relatorio_feedbacks.arn
  protocol  = "email"
  endpoint  = "email-exemplo@email.com"  # endereço que vai receber as mensagens
}

