# Class Review - Serverless Feedback Management Platform

## Sobre o Projeto

Class Review é uma solução serverless desenvolvida para coleta, processamento e análise de feedbacks acadêmicos utilizando serviços gerenciados da AWS.

A plataforma permite o registro de avaliações e comentários, classificação automática de criticidade, processamento assíncrono de ocorrências críticas e geração automatizada de relatórios semanais.

---

## Arquitetura da Solução

A aplicação foi construída utilizando uma arquitetura orientada a eventos baseada em microsserviços serverless.

### Fluxo de Feedbacks

```text
Cliente
   ↓
API Gateway
   ↓
Feedback API (AWS Lambda)
   ↓
DynamoDB

Feedback Crítico
   ↓
Amazon SQS
   ↓
Critical Feedback Consumer (AWS Lambda)
   ↓
Amazon SNS
   ↓
E-mail de Alerta
```

### Fluxo de Relatórios

```text
Amazon EventBridge Scheduler
   ↓
Weekly Report Scheduler (AWS Lambda)
   ↓
DynamoDB
   ↓
Amazon SNS
   ↓
E-mail com Relatório Semanal
```

---

## Tecnologias Utilizadas

### Backend

* Java 21
* Quarkus
* Maven

### Cloud AWS

* AWS Lambda
* Amazon API Gateway
* Amazon DynamoDB
* Amazon SQS
* Amazon SNS
* Amazon EventBridge Scheduler
* Amazon CloudWatch
* AWS CloudFormation
* AWS SAM

### DevOps

* GitHub Actions
* CI/CD
* Infrastructure as Code (IaC)

---

## Estrutura do Projeto

```text
class-review
│
├── api
│   └── API responsável pelo recebimento dos feedbacks
│
├── critical-feedback-consumer
│   └── Processamento assíncrono de feedbacks críticos
│
├── scheduler
│   └── Geração automática de relatórios semanais
│
└── core
    └── Regras de negócio compartilhadas
```

---

## Funcionalidades

### Cadastro de Feedbacks

Endpoint responsável pelo recebimento das avaliações.

#### Exemplo de Requisição

```json
{
  "rating": 4,
  "comment": "professor ruim"
}
```

#### Exemplo de Resposta

```json
{
  "id": "797c459e-6486-4a39-a92b-a677b76260b6",
  "rating": 4,
  "comment": "professor ruim",
  "urgency": "CRITICAL"
}
```

---

### Processamento de Feedbacks Críticos

Feedbacks classificados como críticos são enviados para uma fila SQS para processamento assíncrono.

Após o processamento, notificações são enviadas através do Amazon SNS.

---

### Relatórios Semanais

O sistema gera automaticamente relatórios contendo:

* Média das avaliações
* Quantidade de feedbacks por dia
* Quantidade de feedbacks por nível de urgência
* Resumo dos feedbacks processados

Exemplo simplificado:

```json
{
  "feedbacks": [],
  "feedbacksPerDay": {},
  "feedbacksByUrgency": {},
  "averageRating": 4.2
}
```

---

## Infraestrutura AWS

### Stacks CloudFormation

* class-review-api
* class-review-consumer
* class-review-scheduler

### Recursos Provisionados

#### API

* API Gateway
* AWS Lambda
* DynamoDB
* Amazon SQS

#### Consumer

* AWS Lambda
* Event Source Mapping (SQS → Lambda)

#### Scheduler

* AWS Lambda
* EventBridge Scheduler

---

## Monitoramento e Observabilidade

A solução utiliza Amazon CloudWatch para monitoramento dos serviços.

### Alarmes Configurados

* api-lambda-errors
* api-lambda-duration
* consumer-errors
* scheduler-errors

### Tópicos SNS

* critical-feedback-alerts
* weekly-feedback-report
* system-alerts

---

## CI/CD

O projeto utiliza GitHub Actions para automação de build e deploy.

Cada serviço possui pipeline independente:

* deploy-api.yml
* deploy-consumer.yml
* deploy-scheduler.yml

Fluxo de deploy:

```text
Push na branch main
        ↓
GitHub Actions
        ↓
Build Maven
        ↓
AWS SAM
        ↓
CloudFormation
        ↓
Deploy Automático
```

---

## Deploy Manual

### Build

```bash
./mvnw clean package -DskipTests
```

### Deploy da API

```bash
cd api
sam deploy
```

### Deploy do Consumer

```bash
cd critical-feedback-consumer
sam deploy
```

### Deploy do Scheduler

```bash
cd scheduler
sam deploy
```

---

## Autor

Projeto desenvolvido como parte da disciplina de Arquitetura Cloud e Computação Serverless.

**Joyce Santos**
