# 📧 Relatório Semanal de Feedbacks – Serverless AWS

Este módulo implementa um **processo serverless na AWS** responsável por **gerar e enviar, a cada 7 dias, um relatório resumido de feedbacks por e-mail**, utilizando **AWS Lambda, EventBridge e SNS**, com aplicação desenvolvida em **Java + Quarkus**.

---

## 🏗️ Arquitetura da Solução

A solução utiliza serviços gerenciados da AWS para automação completa do processo de geração e envio do relatório.

**Serviços Utilizados**

- Amazon EventBridge (CloudWatch Events)

    Responsável por disparar o processo semanalmente, conforme agendamento definido.

- AWS Lambda

    Executa a lógica de consulta ao banco de dados, cálculo das métricas e formatação do relatório.

- Amazon RDS (PostgreSQL)

    Armazena os feedbacks que são utilizados como base para o relatório.

- Amazon SNS

    Publica o relatório gerado e realiza o envio do e-mail aos inscritos.

- SNS Subscription (Email)

    Define os endereços de e-mail que receberão o relatório semanal.
---

## 🔄 Fluxo de Funcionamento

1. O EventBridge dispara o evento semanal conforme a expressão cron configurada.
2. A Lambda é acionada automaticamente.
3. A Lambda:
   - Consulta o banco RDS (PostgreSQL)
   - Calcula métricas agregadas (média, quantidade por urgência, etc.)
4. Gera o relatório no formato de texto
5. O relatório é publicado em um tópico SNS.
6. O SNS envia o relatório por e-mail para todos os subscribers cadastrados.

---

## ⏰ Agendamento do Evento (EventBridge)

O agendamento semanal é definido no Terraform através do recurso abaixo:
```
resource "aws_cloudwatch_event_rule" "weekly_event" {
name                = "event-relatorio-sns-weekly"
schedule_expression = "cron(0 3 ? * SUN *)"
}
```

🕒 Esse cron executa o processo todos os domingos às 03:00 (UTC).

---

## 🧱 Infraestrutura como Código (Terraform)

A infraestrutura do projeto é gerenciada com Terraform, organizada nos seguintes arquivos:

- event.tf

    Define o agendamento semanal no EventBridge e a regra que aciona a Lambda.

- iam.tf

    Configura as roles e permissões IAM necessárias para a Lambda acessar RDS, SNS e logs.

- lambda.tf

    Cria a função Lambda responsável por gerar e publicar o relatório.

- main.tf

    Configura o provider AWS e centraliza a inicialização da infraestrutura.

- sns.tf

    Cria o tópico SNS e define as subscriptions para envio do relatório por e-mail.

- variables.tf

    Declara as variáveis utilizadas para parametrizar o ambiente.

---

## 📧 Configuração do E-mail de Subscription (SNS)

Os e-mails que receberão o relatório semanal são configurados no arquivo sns.tf, através do recurso aws_sns_topic_subscription.

Exemplo:
```
resource "aws_sns_topic_subscription" "feedback_email" {
topic_arn = aws_sns_topic.relatorio_feedbacks.arn
protocol  = "email"
endpoint  = "janainafrv@hotmail.com"  # endereço que vai receber as mensagens
}
```

***⚠️ Importante:***

Após o terraform apply, a AWS enviará um e-mail de confirmação para o endereço configurado.

O envio dos relatórios só ocorrerá após a confirmação da inscrição.

---

## 🚀 Pipeline de Deploy (GitHub Actions)

O deploy da infraestrutura é feito automaticamente através de uma GitHub Action, utilizando Terraform.

**Arquivo da Pipeline**

- .github/workflows/deploy-or-destroy.yml

Esse workflow é responsável por executar:

- terraform init

- terraform plan

- terraform apply ou terraform destroy, dependendo da variável configurada.

**Variável**: TF_ACTION

Para subir (provisionar) o projeto na AWS, é necessário:

1. Editar o arquivo:

`.github/workflows/deploy-or-destroy.yml`


2. Alterar a variável:

`TF_ACTION: apply`


3. Fazer commit da alteração.

Subir o commit na branch **develop**.

🔁 O pipeline será acionado automaticamente e realizará o deploy da infraestrutura.

Caso seja necessário destruir os recursos, basta alterar o valor para:

`TF_ACTION: destroy`

---
## 🔐 Autenticação com AWS via OIDC (GitHub Actions)

Este projeto utiliza OIDC (OpenID Connect) para autenticação segura entre o GitHub Actions e a AWS, eliminando a necessidade de armazenar credenciais estáticas (Access Key e Secret Key).

Como funciona

* O GitHub Actions assume uma IAM Role na AWS usando OIDC.
* Essa role possui permissões específicas para executar o Terraform.
* A autenticação ocorre de forma temporária e segura durante a execução da pipeline.

Benefícios do OIDC

* 🔒 Maior segurança (sem secrets sensíveis no repositório)
* ♻️ Credenciais temporárias
* 📋 Controle granular de permissões via IAM
* ✅ Padrão recomendado pela AWS

A configuração do OIDC envolve:

* Provider OIDC do GitHub na AWS
* IAM Role com trust policy para o repositório/branch
* Permissões necessárias para criação dos recursos via Terraform

---

## 🚀 Lambda Handler

### `WeeklyReportHandler`

Responsável por integrar a AWS Lambda com o contexto do Quarkus.

---

## 🧠 Job Principal

### `WeeklyReportJob`

Orquestra todo o fluxo de geração e envio do relatório semanal.

---

## 📊 Geração do Relatório

### `WeeklyReportService`

Consolida os dados da última semana e retorna um `WeeklyReportDTO`.

---

## ✉️ Template do E-mail

### `EmailTemplateService`

Formata o relatório em texto simples, compatível com envio por SNS:

```
Relatório semanal de feedbacks

Data de geração: %s

Avaliação média: %.2f

Feedbacks por dia: %.2f

Quantidade de avaliações por urgência:
ALTA: %s
MEDIA: %s
BAIXA: %s

Justificativa da nota pelos alunos:
%s
            
```

---

## 📢 Publicação no SNS

### `NotificationService`

Publica o relatório em um tópico SNS configurado via `application.properties`.

---

## ⚙️ Requisitos Técnicos

- Java 17+
- Quarkus (Lambda)
- AWS Lambda
- EventBridge
- SNS