# Instruções de Execução da API MAVIS

## 1. Gerar Chaves RSA

Para garantir a segurança das autenticações, gere as chaves RSA da seguinte maneira:

### Gerar a Chave Privada

Execute o comando abaixo para gerar uma chave privada de 4096 bits e salvar no arquivo `app.key`. Você pode ajustar o comprimento da chave e o nome do arquivo conforme necessário:

```bash
$ cd src/main/resources/ 
$ openssl genrsa > app.key 
```

### Derivar a Chave Pública

Com a chave privada gerada, derive a chave pública e salve no arquivo `app.pub`:

```bash
$ openssl rsa -in app.key -pubout -out app.pub
```

**Importante**: As chaves devem ser chamadas `app.key` (privada) e `app.pub` (pública), e devem ser armazenadas no diretório `src/main/resources`.


## 2. Configuração do Arquivo `application.properties`

Configure o arquivo `src/main/resources/application.properties` com as seguintes propriedades:

```properties
# Chaves JWT
jwt.public.key=classpath:app.pub
jwt.private.key=classpath:app.key

# Configurações do Banco de Dados
spring.datasource.url=jdbc:postgresql://${DB_HOST}:${DB_PORT}/${DB_NAME}
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA & Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# Configuração do Servidor (opcional)
server.error.include-stacktrace=never
```

## 3. Executar o Docker Compose

Execute o Docker Compose para iniciar o PostgreSQL. O arquivo `compose.yml` deve ser configurado da seguinte maneira:

```yaml
services:
  postgres:
    image: postgres:15.11-alpine3.20
    container_name: postgres_container
    restart: always
    environment:
      POSTGRES_USER: ${DB_USER}
      POSTGRES_PASSWORD: ${DB_PASSWORD}
      POSTGRES_DB: ${DB_NAME}
    ports:
      - "5432:5432"
```

Após configurar o Docker Compose, execute o comando para subir os serviços:

```bash
$ docker-compose up -d
```

Para verificar se o PostgreSQL está funcionando corretamente, você pode executar:

```bash
$ docker-compose ps
```

E para testar a conexão com o banco de dados, use:

```bash
$ psql -h localhost -U postgres -d mavis
```

## 4. Executar a Aplicação Spring

Agora, execute a aplicação Spring Boot:

```bash
$ ./gradlew bootRun
```



