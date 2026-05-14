# Agendamento de Consultas - Guia Rapido (Dev)

Este guia foi feito para quem esta subindo o projeto pela primeira vez.

## 1) Clone e primeira subida (faca isso primeiro)

### Pre-requisitos

- Java 21 instalado
- Docker Desktop instalado e aberto
- Projeto clonado

### Passo a passo

1. Entre na pasta do projeto.
2. Crie o arquivo `.env` na raiz (com as variaveis do time).
3. Rode o comando:

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\dev-up.ps1
```

Pronto. Esse script faz 3 coisas:
- sobe o PostgreSQL no Docker;
- espera o banco ficar pronto;
- inicia a API Spring Boot.

## Como saber se deu certo

- No terminal, a aplicacao termina de subir sem erro.
- Acesse essa URL no navegador:

`http://localhost:8080/swagger-ui.html`

## No dia a dia

Depois da primeira vez, pode usar o mesmo comando:

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\dev-up.ps1
```

## Como parar a aplicacao

- Para parar a API: `Ctrl + C` no terminal onde ela esta rodando.
- Para parar o banco Docker:

```powershell
docker compose down
```

## Erros comuns e solucao

### 1) Erro de conexao com banco (porta 5432)

Verifique se o Docker Desktop esta aberto e rode:

```powershell
docker compose up -d
```

### 2) Container do Postgres sobe e cai

Recrie o ambiente local do banco:

```powershell
docker compose down -v
docker compose up -d
```

Atencao: `down -v` apaga os dados locais do banco.

### 3) Funciona no script, mas nao funciona no botao Run da IDE

O script carrega o `.env` automaticamente.
Na IDE, voce precisa configurar as mesmas variaveis de ambiente na Run Configuration.

## O que o projeto faz e como usar no Swagger, Postman ou no Bruno API Client

### URL da documentacao

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

### Funcionalidades principais

- Autenticacao JWT com `login`, `refresh` e `logout`.
- Bootstrap do primeiro admin com `X-Bootstrap-Key`.
- Cadastro de usuarios medicos (rota protegida para `ADMIN`).
- CRUD de pacientes.
- CRUD de contatos de pacientes.
- CRUD de agendamentos.
- Reagendamento e cancelamento com regras de negocio.
- Validacoes para evitar sobreposicao de horarios e datas passadas.
- Notificacoes de confirmacao, atualizacao, cancelamento, lembrete e agradecimento.
- Integracao ViaCEP para consulta de endereco por CEP.
- Integracao Twilio (modo real ou simulado).
- Controle de acesso por perfil (`ADMIN` e `MEDICO`).

### Como testar rapido no Swagger

1. Crie o primeiro admin em `POST /v1/auth/bootstrap-admin` usando header `X-Bootstrap-Key`.
2. Faca login em `POST /v1/auth/login` e copie o `accessToken`.
3. Clique em `Authorize` no Swagger e cole: `Bearer <accessToken>`.
4. Teste endpoints de negocio:
- `/v1/pacientes`
- `/v1/contatos`
- `/v1/agendamentos`
- `/v1/enderecos/cep/{cep}`

### Como usar no Postman

1. Crie um Environment (ex.: `agendamento-local`) com:
- `baseUrl = http://localhost:8080`
- `bootstrapKey = <valor do BOOTSTRAP_ADMIN_KEY>`
- `token =` (deixe vazio no inicio)
2. Crie a request `Bootstrap Admin`:
- `POST {{baseUrl}}/v1/auth/bootstrap-admin`
- Header: `X-Bootstrap-Key: {{bootstrapKey}}`
- Body (JSON) com email e senha do admin.
3. Crie a request `Login`:
- `POST {{baseUrl}}/v1/auth/login`
- Body (JSON) com email e senha.
- Salve o `accessToken` retornado em `token`.
4. Nas requests protegidas, use Authorization `Bearer Token` com `{{token}}`.
5. Teste os endpoints:
- `{{baseUrl}}/v1/pacientes`
- `{{baseUrl}}/v1/contatos`
- `{{baseUrl}}/v1/agendamentos`
- `{{baseUrl}}/v1/enderecos/cep/01001000`

### Como usar no Bruno API Client

1. Crie uma Collection (ex.: `agendamento-consultas`).
2. Crie variaveis de ambiente:
- `baseUrl = http://localhost:8080`
- `bootstrapKey = <valor do BOOTSTRAP_ADMIN_KEY>`
- `token =` (vazio no inicio)
3. Crie a request `Bootstrap Admin`:
- `POST {{baseUrl}}/v1/auth/bootstrap-admin`
- Header: `X-Bootstrap-Key: {{bootstrapKey}}`
- Body JSON com email e senha.
4. Crie a request `Login`:
- `POST {{baseUrl}}/v1/auth/login`
- Body JSON com email e senha.
- Copie o `accessToken` para a variavel `token`.
5. Nas requests protegidas, adicione header:
- `Authorization: Bearer {{token}}`
6. Teste os mesmos endpoints de negocio (`pacientes`, `contatos`, `agendamentos`, `enderecos`).
