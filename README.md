# MAVIS – Medical Assistance Vital Information System

---

A **API MAVIS** é o backend do sistema MAVIS, responsável pelo gerenciamento do perfil do usuário e informações essenciais para emergências.

O projeto segue uma arquitetura baseada em camadas:
- **Controller**: Camada responsável por receber requisições HTTP e retornar respostas.
- **Service**: Contém a lógica de negócio da aplicação.
- **Repository**: Interage diretamente com o banco de dados usando o Spring Data JPA.

A API é desenvolvida em **Spring Boot** e utiliza **PostgreSQL** como banco de dados. Para simplificar a execução, os serviços são disponibilizados via **Docker Compose**.

---

## Envio de Notificações de Emergência

Ao realizar uma requisição `POST` para o endpoint `/api/notifications/send/`, a API acionará um pedido de ajuda (emergência), notificando os contatos de emergência do usuário. As notificações serão enviadas por e-mail, caso o contato tenha um endereço cadastrado, e via SMS para o número de telefone registrado. Esse mecanismo assegura que os contatos de emergência sejam informados de maneira rápida e eficiente em situações críticas.

---

## **Instruções de Execução da API MAVIS**

### 1. Clonar o Repositório

Antes de gerar as chaves RSA, é necessário clonar o repositório do projeto:

```bash
git clone https://github.com/nataliatsi/mavis-backend.git
cd mavis
```

---

### 2. Gerar Chaves RSA

Para garantir a segurança das autenticações, gere as chaves RSA da seguinte maneira:

#### Gerar a Chave Privada

Execute o comando abaixo para gerar uma chave privada e salvar no arquivo `app.key` dentro do diretório `src/main/resources/`:

```bash
openssl genrsa -out src/main/resources/app.key
```

#### Derivar a Chave Pública

Com a chave privada gerada, derive a chave pública e salve no arquivo `app.pub` dentro do mesmo diretório:

```bash
openssl rsa -in src/main/resources/app.key -pubout -out src/main/resources/app.pub
```

**Importante**: As chaves devem ser chamadas `app.key` (privada) e `app.pub` (pública), e devem ser armazenadas no diretório `src/main/resources`.

---

### 3. Executar a API com Docker Compose

Agora, a execução da API e do banco de dados é feita de forma simplificada com **Docker Compose**.

### Construir e subir os serviços

```bash
docker-compose up --build -d
```

Isso irá:
- Construir e rodar a API MAVIS.
- Iniciar o banco de dados PostgreSQL.

A API estará acessível em `http://localhost:8080`.

---

## Dependências

Para o funcionamento correto do envio de notificações, é necessário configurar os seguintes serviços externos:
- **[Twilio](https://www.twilio.com/pt-br)**: Utilizado para o envio de mensagens SMS. 
- **[Mailtrap](https://mailtrap.io/)**: Utilizado para o envio de e-mails em ambiente de teste. 

É importante seguir as instruções de cada serviço para configurar corretamente as variáveis de ambiente com suas credenciais.

---

## **Desenvolvedores**

Este projeto foi desenvolvido por:

- **[Backend - Natália Gomes](https://github.com/nataliatsi)**
- **[Frontend - João Igor](https://github.com/ignizxl)**





