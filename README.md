# 📚 Literalura - API de Catálogo de Livros

![Status](https://img.shields.io/badge/status-ativo-brightgreen)
![Java](https://img.shields.io/badge/Java-17-blue)
![Spring](https://img.shields.io/badge/Spring_Boot-3.x-green)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-14-blue)

## 📖 Sobre o Projeto

**Literalura** é uma API RESTful desenvolvida como parte do Challenge Back-End da Alura. A aplicação funciona como um catálogo interativo de livros, consumindo dados da API pública [Gutendex](https://gutendex.com/). Ela permite buscar, salvar e consultar informações sobre livros e autores, persistindo todos os dados em um banco de dados PostgreSQL.

O projeto foi estruturado para ser robusto e escalável, utilizando as melhores práticas do ecossistema Spring, como injeção de dependência, DTOs (Data Transfer Objects) para a comunicação com o cliente e tratamento de exceções centralizado.

## ✨ Funcionalidades Principais da API

*   **Busca e Persistência:** Busca livros na API Gutendex pelo título e os salva no banco de dados local.
*   **Paginação e Listagem:** Lista livros e autores registrados com suporte a paginação.
*   **Filtros Avançados:** Permite listar autores vivos em um determinado ano e livros por idioma.
*   **Consultas Específicas:** Oferece endpoints para buscar o Top 10 livros mais baixados e estatísticas gerais.
*   **Tratamento de Erros:** Implementa um sistema de tratamento de exceções para fornecer respostas de erro claras e consistentes.
*   **Configuração de CORS:** Preparada para ser consumida por frontends hospedados em domínios diferentes.

## 🛠️ Tecnologias Utilizadas

*   **Java 17 & Spring Boot 3:** Base da aplicação, oferecendo um ambiente robusto e moderno.
*   **Spring Data JPA & Hibernate:** Para persistência de dados e mapeamento objeto-relacional.
*   **PostgreSQL:** Banco de dados relacional para armazenamento das informações.
*   **Maven:** Gerenciador de dependências e build do projeto.
*   **Jackson:** Biblioteca para manipulação de JSON.
*   **DTO (Data Transfer Objects):** Padrão utilizado para transferir dados entre as camadas da aplicação e o cliente.

## 🚀 Como Executar a API Localmente

### Pré-requisitos

*   [Git](https://git-scm.com)
*   [JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) ou superior.
*   [Maven](https://maven.apache.org/download.cgi)
*   Um SGBD **PostgreSQL** rodando localmente.

### Passos

1.  **Clone o repositório:**
    ```bash
    git clone https://github.com/SilviaAvelar/literalura-backend.git
    cd literalura-backend
    ```

2.  **Configure o banco de dados:**
    *   Crie um banco de dados no seu PostgreSQL chamado `literalura`.
    *   Na pasta `src/main/resources/`, crie o arquivo `application-api-local.properties` (se não existir) com suas credenciais:
    ```properties
    spring.datasource.url=jdbc:postgresql://localhost:5432/literalura
    spring.datasource.username=seu_usuario_postgres
    spring.datasource.password=sua_senha
    spring.jpa.hibernate.ddl-auto=update
    ```

3.  **Configure o perfil ativo na sua IDE (IntelliJ IDEA):**
    *   Vá em `Run` -> `Edit Configurations...`.
    *   Clique em `Modify options` -> `Add VM options`.
    *   No campo `VM options`, cole: `-Dspring.profiles.active=api-local`
    *   Clique em `Apply` e `OK`.

4.  **Execute a aplicação:**
    *   Rode a classe principal `LiteraluraApplication`. O servidor iniciará na porta `8080`.

## 🌐 Endpoints da API

Você pode testar os endpoints usando ferramentas como [Insomnia](https://insomnia.rest/) ou [Postman](https://www.postman.com/).

**Base URL:** `http://localhost:8080/api`

| Verbo  | Endpoint                                   | Descrição                                         | Exemplo de Uso                               |
|--------|--------------------------------------------|---------------------------------------------------|----------------------------------------------|
| `POST` | `/livros/buscar-salvar/titulo/{titulo}`      | Busca um livro na API externa e o salva.        | `.../livros/buscar-salvar/titulo/don%20quixote` |
| `GET`  | `/livros`                                  | Lista todos os livros registrados (com paginação). | `.../livros?page=0&size=5`                     |
| `GET`  | `/autores`                                 | Lista todos os autores (com paginação).           | `.../autores?sort=nome,asc`                    |
| `GET`  | `/livros/idioma/{sigla}`                   | Lista livros por idioma (ex: en, pt, es).         | `.../livros/idioma/es`                         |
| `GET`  | `/autores/vivos/{ano}`                     | Lista autores vivos em um determinado ano.        | `.../autores/vivos/1850`                       |
| `GET`  | `/livros/top10`                            | Retorna o Top 10 livros mais baixados.            | `.../livros/top10`                             |
| `GET`  | `/livros/stats`                            | Retorna estatísticas de downloads.                | `.../livros/stats`                             |
| `GET`  | `/hello`                                   | Endpoint de teste para verificar se a API está no ar. | `.../hello`                                  |

## ✒️ Autora

Desenvolvido com dedicação por **Silvia Avelar**.

[![LinkedIn](https://img.shields.io/badge/LinkedIn-Silvia_Avelar-blue)](https://www.linkedin.com/in/silvia-avelar/)
[![GitHub](https://img.shields.io/badge/GitHub-SilviaAvelar-black)](https://github.com/SilviaAvelar)
[![Portfolio](https://img.shields.io/badge/Portfolio-Online-blueviolet)](https://silviaavelar.github.io/Portfolio/)
