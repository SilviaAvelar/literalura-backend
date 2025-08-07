# Estágio 1: Build da aplicação com Maven
# Usamos uma imagem oficial do Maven com o JDK 17
FROM maven:3.9-eclipse-temurin-17 AS build

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copia o pom.xml para baixar as dependências primeiro (otimização de cache)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia o resto do código-fonte
COPY src ./src

# Compila e empacota a aplicação, pulando os testes
RUN mvn package -DskipTests

# Estágio 2: Execução da aplicação
# Usamos uma imagem leve, apenas com o Java 17, para rodar a aplicação
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Copia o arquivo .jar gerado no estágio de build
COPY --from=build /app/target/literalura-0.0.1-SNAPSHOT.jar app.jar

# Expõe a porta 8080 (a porta padrão do Spring Boot)
EXPOSE 8080

# Comando para iniciar a aplicação quando o container rodar
ENTRYPOINT ["java", "-jar", "app.jar"]