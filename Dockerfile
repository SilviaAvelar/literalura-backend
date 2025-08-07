# Estágio 1: Build da aplicação com Maven
# Usamos uma imagem oficial do OpenJDK 17
FROM openjdk:17-jdk-slim AS build

# Define o diretório de trabalho
WORKDIR /app

# Copia os arquivos do Maven Wrapper primeiro
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Dá permissão de execução ao mvnw dentro do container
RUN chmod +x ./mvnw

# Baixa as dependências (isso será cacheado se o pom.xml não mudar)
RUN ./mvnw dependency:go-offline

# Copia o resto do código-fonte
COPY src ./src

# Compila e empacota a aplicação
RUN ./mvnw package -DskipTests


# Estágio 2: Execução da aplicação
# Usamos uma imagem JRE leve para rodar
FROM openjdk:17-jre-slim

WORKDIR /app

# Copia o arquivo .jar gerado no estágio de build
COPY --from=build /app/target/literalura-0.0.1-SNAPSHOT.jar app.jar

# Expõe a porta que o Spring Boot usa
EXPOSE 8080

# Comando para iniciar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]