# Instruções de Execução da API MAVIS

## 1. Sobre o Projeto

A **API MAVIS** é o backend do sistema MAVIS, responsável pelo gerenciamento do perfil do usuário e informações essenciais para emergências.

O projeto segue uma arquitetura baseada em camadas:
- **Controller**: Camada responsável por receber requisições HTTP e retornar respostas.
- **Service**: Contém a lógica de negócio da aplicação.
- **Repository**: Interage diretamente com o banco de dados usando o Spring Data JPA.

A API é desenvolvida em **Spring Boot** e utiliza **PostgreSQL** como banco de dados. Para simplificar a execução, os serviços são disponibilizados via **Docker Compose**.

---

## 2. Clonar o Repositório

Antes de gerar as chaves RSA, é necessário clonar o repositório do projeto:

```bash
git clone https://github.com/nataliatsi/mavis-backend.git
cd mavis
```

---

## 3. Gerar Chaves RSA

Para garantir a segurança das autenticações, gere as chaves RSA da seguinte maneira:

### Gerar a Chave Privada

Execute o comando abaixo para gerar uma chave privada e salvar no arquivo `app.key` dentro do diretório `src/main/resources/`:

```bash
openssl genrsa -out src/main/resources/app.key
```

### Derivar a Chave Pública

Com a chave privada gerada, derive a chave pública e salve no arquivo `app.pub` dentro do mesmo diretório:

```bash
openssl rsa -in src/main/resources/app.key -pubout -out src/main/resources/app.pub
```

**Importante**: As chaves devem ser chamadas `app.key` (privada) e `app.pub` (pública), e devem ser armazenadas no diretório `src/main/resources`.

---

## 4. Executar a API com Docker Compose

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

### **Desenvolvedores**

Este projeto foi desenvolvido por:

- **[Backend - Natália Gomes](https://github.com/nataliatsi)**
- **[Frontend - João Igor](https://github.com/ignizxl)**





