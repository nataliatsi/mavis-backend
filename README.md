# MAVIS – Medical Assistance Vital Information System

---

<p align="center">
    <img src="https://img.shields.io/badge/Status-Em%20Refatoração-0080ff?style=flat">
    <img src="https://img.shields.io/badge/Java-17-0080ff?style=flat&logo=java&logoColor=white">
    <img src="https://img.shields.io/badge/Spring%20Boot-3.x-0080ff?style=flat&logo=spring-boot&logoColor=white">
    <img src="https://img.shields.io/badge/PostgreSQL-0080ff?style=flat&logo=postgresql&logoColor=white">
    <img src="https://img.shields.io/badge/Docker-0080ff?style=flat&logo=docker&logoColor=white">
</p>

---

## Sumário

* [Visão Geral](#visão-geral)
* [Arquitetura](#arquitetura)
* [Endpoints da API](#endpoints-da-api)
* [Autenticação](#autenticação)
* [Validação dos Dados](#validação-dos-dados)
* [Como Executar](#como-executar)

    * [Pré-requisitos](#pré-requisitos)
    * [Gerar Chaves RSA](#gerar-chaves-rsa)
    * [Executando com Docker Compose](#executando-com-docker-compose)
    * [Testando a Aplicação](#testando-a-aplicação)
* [Documentação da API](#documentação-da-api)
* [Desenvolvedores](#desenvolvedores)

---

## Visão Geral

A **API MAVIS** é o backend do sistema **Medical Assistance Vital Information System**, responsável pelo gerenciamento de perfis de usuários e pelas notificações em situações de emergência.

O sistema permite que o usuário acione um **pedido de ajuda**, enviando notificações aos contatos de emergência por **e-mail** e **SMS**, garantindo resposta rápida em situações críticas.

---

## Arquitetura

A API MAVIS segue uma **arquitetura em camadas**, promovendo **organização, reuso e separação de responsabilidades**:

* **Controller** → recebe as requisições HTTP e retorna as respostas adequadas.
* **Service** → implementa a lógica de negócio da aplicação.
* **Repository** → lida com a persistência e o acesso aos dados usando **Spring Data JPA**.

O projeto é desenvolvido em **Spring Boot** com **PostgreSQL** como banco de dados, e a execução é facilitada por **Docker Compose**.

---

## Endpoints da API

| Método    | Endpoint                  | Descrição                                                 | Autenticação   |
| --------- |---------------------------| --------------------------------------------------------- | -------------- |
| **POST**  | `/api/v2/users`           | Cria um novo usuário                                      | ❌ Não requer   |
| **POST**  | `/api/login`              | Autentica o usuário e retorna um token JWT                | ✅ Basic Auth   |
| **PATCH** | `/api/v2/users/password`  | Altera a senha do usuário autenticado                     | ✅ Bearer Token |

---

## Autenticação

A segurança da API é garantida por **Spring Security** e **JWT assinado com RSA**.

### Fluxo de autenticação

1. O usuário realiza login com **Basic Auth** (`email` e `senha`).
2. Se as credenciais forem válidas, a API retorna um **token JWT**.
3. Esse token deve ser incluído nas requisições subsequentes no cabeçalho:

```
Authorization: Bearer SEU_TOKEN_JWT
```

### Geração das chaves RSA

As chaves `app.key` (privada) e `app.pub` (pública) são usadas para **assinar e validar o JWT**.
Elas devem ser armazenadas em `src/main/resources`.

---

## Validação dos Dados

A API utiliza **Jakarta Bean Validation** para garantir integridade e consistência nas entradas de dados.

* Os DTOs (Data Transfer Objects) contêm anotações como `@NotNull`, `@Email`, `@Size`, entre outras.
* A validação ocorre automaticamente antes da execução dos serviços.
* Erros de validação retornam respostas claras com mensagens amigáveis.

Exemplo:

```json
{
  "timestamp": "2025-10-10T12:34:56",
  "status": 400,
  "errors": ["O campo 'email' é obrigatório e deve estar em formato válido."]
}
```

---

## Como Executar

### Pré-requisitos

Antes de iniciar, instale e configure:

* **Java 17+**
* **Docker e Docker Compose**
* **OpenSSL** (para gerar as chaves RSA)

### Gerar Chaves RSA

1. Gere a chave privada:

```bash
openssl genrsa -out src/main/resources/app.key
```

2. Gere a chave pública:

```bash
openssl rsa -in src/main/resources/app.key -pubout -out src/main/resources/app.pub
```

---

### Executando com Docker Compose

Clone o repositório e entre no diretório do projeto:

```bash
git clone https://github.com/nataliatsi/mavis-backend.git
cd mavis
```

Construa e suba os serviços:

```bash
docker-compose up --build -d
```

A API estará disponível em:

👉 [http://localhost:8080](http://localhost:8080)

---

### Testando a Aplicação

A MAVIS utiliza **Gradle Wrapper (`gradlew`)** para execução dos testes automatizados.

Execute os testes com:

```bash
./gradlew test
```

Para limpar e testar novamente:

```bash
./gradlew clean test
```

---

## Documentação da API

Após iniciar a aplicação, acesse o **Swagger UI**:

🔗 **[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)**

Aqui é possível visualizar e testar os endpoints disponíveis diretamente pelo navegador.

---

## Desenvolvedores

Este projeto foi desenvolvido por:

* **Backend — [Natália Gomes](https://github.com/nataliatsi)**
* **Frontend — [João Igor](https://github.com/ignizxl)**

---

<div align="center">

[↑ **Voltar ao topo** ↑](#mavis--medical-assistance-vital-information-system)

</div>





