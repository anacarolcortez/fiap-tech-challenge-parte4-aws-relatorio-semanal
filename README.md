# 📧 Relatório Semanal de Feedbacks – Serverless AWS

Este módulo implementa um **processo serverless na AWS** responsável por **gerar e enviar, a cada 7 dias, um relatório resumido de feedbacks por e-mail**, utilizando **AWS Lambda, EventBridge e SNS**, com aplicação desenvolvida em **Java + Quarkus**.

---

## 🏗️ Arquitetura

Fluxo geral do sistema:

```
EventBridge (rate: 7 days)
        ↓
AWS Lambda (Quarkus)
        ↓
WeeklyReportJob
        ↓
WeeklyReportService → Repository
        ↓
EmailTemplateService
        ↓
SNS Topic
        ↓
E-mail para assinantes
```

### Serviços AWS utilizados
- **AWS EventBridge**: agenda a execução semanal
- **AWS Lambda**: executa o job serverless
- **Amazon SNS**: publica a mensagem e envia o e-mail

---

## ⏰ Agendamento

A Lambda é acionada automaticamente pelo **EventBridge**, configurado com uma expressão do tipo:

```
rate(7 days)
```

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