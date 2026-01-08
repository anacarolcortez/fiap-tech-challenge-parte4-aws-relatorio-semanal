variable "aws_region" {
  description = "Região AWS"
  type        = string
  default     = "us-east-1"
}

variable "tb_name" {
  type        = string
  default     = "items"
  description = "Nome da tabela"
}

variable "db_name" {
  type        = string
  default     = "itemsdb"
  description = "Nome do banco de dados"
}

variable "db_username" {
  type        = string
  default     = "dbadmin"
  description = "Usuário do banco"
}

variable "db_password" {
  type        = string
  default     = "dbadmin1234"
  description = "Senha do banco"
  sensitive   = true
}

variable "vpc_subnet_ids" {
  type = list(string)
}

variable "lambda_security_group_id" {
  type = string
}
