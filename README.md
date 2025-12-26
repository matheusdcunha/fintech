# Fintech

Esse é um projeto educacional, para entender melhor exceções e validações no Spring Boot.

O intuito é compreender mais a fundo sobre tratamento de exceções, a arquitetura do Spring Boot e validações. O projeto é uma fintech, podendo criar e deletar conta, fazer depósitos, transações e retirar extratos. Tudo isso respeitando conceitos de ACID.

## Tecnologias Utilizadas

- **Java** `21`
- **Spring Boot** `3.5.9`
- **Spring Data JPA**
- **Hibernate Validation**
- **MySQL**
- **Docker**
- **Maven**

## Pré-requisitos

Antes de iniciar o projeto, certifique-se de ter instalado:

- **Java 21** ou superior
- **Docker** e **Docker Compose**
- **Maven** (opcional, pois o projeto inclui o Maven Wrapper)

## Como iniciar o projeto

### 1. Iniciar o banco de dados

Para iniciar o banco de dados MySQL, execute o seguinte comando:

```sh
docker-compose up -d db_fintech
```

Este comando irá:
- Criar um container PostgreSQL chamado `db_fintech`
- Expor a porta `3306`
- Criar o banco de dados `db_fintech`
- Configurar as credenciais padrão (usuário: `matheus`, senha: `secret`)

### 2. Executar a aplicação

Você pode executar a aplicação de duas formas:

**Usando Maven Wrapper (recomendado):**

```sh
./mvnw spring-boot:run
```

**Ou usando Maven instalado:**

```sh
mvn spring-boot:run
```

A aplicação será iniciada na porta padrão `8080`.

### 3. Verificar se está funcionando

Após iniciar a aplicação, você pode verificar se está tudo funcionando acessando:

```
http://localhost:8080
```

## Documentação

### Sobre o sistema

Pode ver mais sobre o sistema e como ele se propõe no arquivo [`fintech.md`](/docs/fintech.md).

### Rotas

Todas as rotas dessa API podem ser encontradas no arquivo [routes.http](./docs/api/routes.http).

Pode usar a extensão HTTP Client para fazer requisição diretamente do arquivo `.http`.

### Banco de Dados

Os diagramas podem ser encontrados em [`./docs/database`](./docs/database), lá se encontra um diagrama feito em `.MERMAID` no arquivo [DER.MD](./docs/database/DER.MD) e outro feito em `.dbml` no arquivo [fintech_db.dbml](./docs/database/fintech_db.dbml).
