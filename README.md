# Product Management API

API REST desenvolvida em Java e Spring Boot para gerenciamento de produtos, clientes e vendas, com autenticação e autorização de usuários.

O projeto está sendo desenvolvido com base em um cenário real de vendas de cosméticos de diferentes marcas e categorias. A aplicação tem como objetivo centralizar informações que atualmente são controladas manualmente, como produtos, estoque, vendas, clientes, pagamentos e valores a receber.

> **Status:** Em desenvolvimento 🚧  
> A aplicação está sendo construída e evoluída gradualmente conforme os requisitos e regras de negócio são definidos.

---

## 🎯 Objetivo

O projeto surgiu a partir de uma necessidade real de organizar o processo de vendas de cosméticos.

Entre os principais problemas que a aplicação pretende solucionar estão:

- dificuldade para controlar o estoque;
- falta de histórico organizado das vendas;
- dificuldade para acompanhar clientes e suas compras;
- dificuldade para controlar vendas parceladas;
- dificuldade para identificar valores pendentes;
- acompanhamento de pagamentos e parcelas;
- análise de produtos com maior ou menor saída;
- controle de custos, lucros e perdas.

A proposta é transformar esses processos manuais em uma aplicação centralizada, mantendo os dados organizados e facilitando a tomada de decisões.

---

## 🚀 Funcionalidades

### 🔐 Autenticação e autorização

- Cadastro de usuários;
- Login;
- Geração de access token;
- Refresh token;
- Autenticação baseada em JWT;
- Senhas protegidas com BCrypt;
- Controle de acesso baseado em roles;
- Hierarquia de permissões entre usuários.

### 👥 Clientes

- Cadastro de clientes;
- Consulta de clientes;
- Atualização de clientes;
- Exclusão de clientes;
- Validação de CPF;
- Validação de telefone;
- Validação de dados duplicados;
- Proteção contra exclusão de clientes vinculados a vendas.

### 📦 Produtos

A funcionalidade de gerenciamento de produtos ainda está em desenvolvimento.

Está prevista a evolução para contemplar:

- cadastro e manutenção de produtos;
- categorias;
- marcas;
- preço de venda;
- custo;
- estoque;
- movimentações de estoque;
- registro de perdas;
- histórico de alterações.

### 🛒 Vendas

A funcionalidade de vendas ainda está em desenvolvimento.

Entre os requisitos previstos estão:

- criação de vendas;
- associação de produtos à venda;
- controle de quantidade;
- atualização do estoque;
- cancelamento;
- histórico de vendas;
- cálculo de valores;
- acompanhamento de custos e lucro.

### 💳 Pagamentos

O domínio de pagamentos e parcelamentos ainda está em fase de definição.

Entre as necessidades identificadas estão:

- vendas à vista;
- vendas parceladas;
- controle de parcelas;
- pagamentos parciais;
- datas de vencimento;
- pagamentos pendentes;
- pagamentos atrasados;
- histórico de pagamentos;
- valores a receber.

### 📊 Relatórios

Também estão previstos relatórios para apoiar a análise das vendas, como:

- produtos mais vendidos;
- produtos menos vendidos;
- vendas por período;
- lucro;
- perdas;
- valores a receber;
- clientes inadimplentes.

> As funcionalidades descritas como "previstas" ainda não devem ser consideradas implementadas.

---

## 🛠️ Tecnologias

| Tecnologia | Utilização |
|---|---|
| Java 21 | Linguagem principal |
| Spring Boot | Desenvolvimento da aplicação |
| Spring Data JPA | Persistência de dados |
| Hibernate | ORM |
| PostgreSQL | Banco de dados principal |
| H2 | Banco utilizado nos testes |
| Spring Security | Autenticação e autorização |
| JWT | Autenticação baseada em tokens |
| BCrypt | Hash de senhas |
| MapStruct | Mapeamento entre DTOs e entidades |
| Maven | Gerenciamento de dependências e build |
| JUnit 5 | Testes automatizados |
| Mockito | Testes unitários e mocks |
| SpringDoc OpenAPI | Documentação da API |

---

## 🏗️ Arquitetura

O projeto utiliza uma organização em camadas, separando responsabilidades entre diferentes componentes da aplicação.

```text
src/main/java/com/alessandromelo/
├── config/
├── controller/
├── dto/
├── entity/
├── enums/
├── exception/
├── exceptionhandler/
├── mapper/
├── repository/
├── security/
└── service/
```

De forma geral:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
Database
```

Os DTOs, mappers, entidades, exceções e componentes de segurança complementam essa estrutura.

---

## 🗄️ Modelagem de Dados

A modelagem foi desenvolvida em diferentes níveis, partindo da representação conceitual do domínio até sua representação lógica.

### Modelo Conceitual

Representa as principais entidades do domínio e seus relacionamentos.
<img width="1657" height="378" alt="Modelo Conceitual" src="https://github.com/user-attachments/assets/d545c584-ee8b-4c2b-9a38-c02f9238ae31" />


### Modelo Lógico

Representa a estrutura lógica atual do banco de dados, incluindo atributos, chaves e relacionamentos.
<img width="1111" height="388" alt="Modelo Lógico" src="https://github.com/user-attachments/assets/d948674e-323d-4109-b7d9-d1e229a75f54" />

> **Nota sobre a modelagem:** os modelos apresentados representam o estado atual do projeto e não devem ser considerados definitivos. Conforme novos requisitos e regras de negócio forem identificados, a estrutura do banco poderá evoluir. Sempre que houver uma alteração relevante na modelagem, os diagramas serão atualizados.

---

## 🧩 Domínio atual

Atualmente, o domínio possui os seguintes conceitos principais:

### Customer

Representa o cliente que realiza compras.

Possui atualmente informações como:

- `id`
- `name`
- `phone_number`
- `cpf`

Um cliente pode possuir várias vendas.

### Product

Representa um produto comercializado.

Possui atualmente:

- `id`
- `name`
- `product_category`
- `price`

O domínio de produtos ainda será expandido conforme os requisitos de estoque, custos, marcas e categorias forem definidos.

### Sale

Representa uma venda realizada para um cliente.

Possui atualmente conceitos relacionados a:

- status;
- valor total;
- quantidade de parcelas;
- valor da parcela;
- data da venda;
- cliente.

### Sale_Product

Representa os produtos pertencentes a uma venda.

Possui:

- quantidade;
- preço unitário;
- venda;
- produto.

Essa entidade permite representar a relação entre vendas e produtos e armazenar informações específicas de cada item vendido.

---

## 🔒 Segurança

A API utiliza Spring Security para controle de autenticação e autorização.

A autenticação utiliza JWT e as senhas dos usuários são armazenadas utilizando BCrypt.

O projeto também possui diferentes níveis de acesso por meio de roles:

```text
OWNER
  ↓
ADMIN
  ↓
USER
```

A autorização é aplicada de acordo com as permissões definidas para cada nível de usuário.

---

## 🧪 Testes

O projeto utiliza JUnit 5 e Mockito para testes automatizados.

Também são utilizados testes de integração para validar o comportamento de componentes da aplicação em conjunto.

Entre os componentes que já possuem testes estão:

- serviços;
- controllers;
- repositories;
- autenticação;
- JWT;
- filtros de segurança.

O objetivo dos testes é validar o comportamento da aplicação e suas regras, e não apenas atingir uma determinada porcentagem de cobertura.

---

## ⚙️ Como executar

### Pré-requisitos

Antes de executar o projeto, é necessário ter instalado:

- Java 21;
- Maven;
- PostgreSQL.

### Banco de dados

Crie um banco PostgreSQL para a aplicação e configure as propriedades de conexão no arquivo de configuração da aplicação.

**Não versione credenciais, senhas ou secrets reais no repositório.**

### Executando a aplicação

Clone o repositório e entre na pasta do projeto:

```bash
git clone git@github.com:alessandromelo22/product-management.git
cd product-management
```

Execute a aplicação com Maven:

```bash
./mvnw spring-boot:run
```

No Windows, caso necessário:

```bash
mvnw.cmd spring-boot:run
```

> A URL do repositório e demais configurações de ambiente devem ser ajustadas conforme o ambiente utilizado.

---

## 📖 Documentação da API

A API utiliza SpringDoc OpenAPI para documentação.

Com a aplicação em execução, a documentação poderá ser acessada pelo endpoint disponibilizado pelo SpringDoc no ambiente local.

---

## 📌 Status do desenvolvimento

| Área | Status |
|---|---|
| Autenticação | ✅ Implementada |
| Autorização | ✅ Implementada |
| Gerenciamento de clientes | ✅ Implementado |
| Gerenciamento de produtos | 🚧 Em desenvolvimento |
| Estoque | 📋 Planejado |
| Vendas | 🚧 Em desenvolvimento |
| Pagamentos | 📋 Em definição |
| Parcelamentos | 📋 Em definição |
| Relatórios | 📋 Planejado |
| Deploy/produção | 📋 Planejado |

---

## 🗺️ Próximos passos

O desenvolvimento seguirá de forma incremental, priorizando a definição das regras de negócio antes da implementação.

Próximas áreas de evolução:

1. Gerenciamento de produtos;
2. Definição do domínio de estoque;
3. Implementação das vendas;
4. Modelagem de pagamentos e parcelamentos;
5. Controle de valores a receber;
6. Relatórios;
7. Revisão e evolução da modelagem;
8. Preparação para ambiente de produção.

---

## 📚 Objetivo de aprendizado

Além de resolver um problema real, este projeto também faz parte do meu processo de desenvolvimento como desenvolvedor Backend Java.

Durante sua construção, estou buscando aplicar e aprofundar conhecimentos em:

- Java;
- Spring e Spring Boot;
- APIs REST;
- modelagem de dados;
- SQL e PostgreSQL;
- JPA e Hibernate;
- segurança de APIs;
- testes automatizados;
- tratamento de exceções;
- arquitetura em camadas;
- Git e GitHub;
- documentação de software;
- análise de requisitos;
- regras de negócio.

A aplicação está sendo desenvolvida de forma incremental, buscando compreender as decisões técnicas e de negócio por trás de cada funcionalidade.

---

## 📄 Licença

Este projeto está em desenvolvimento para fins de estudo e aplicação prática. A definição da licença será realizada posteriormente.
