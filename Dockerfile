# Usa uma imagem base oficial com Maven e Java (JDK 21)
FROM maven:3.9.6-eclipse-temurin-21 AS build

# Define o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copia o pom.xml para baixar as dependências primeiro (cache)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia o resto do código-fonte
COPY src ./src

# Executa o build do Maven para criar o .jar
RUN mvn package -DskipTests

# Estágio final: usa uma imagem base leve, apenas com o ambiente de execução do Java
FROM eclipse-temurin:21-jre-jammy

# Expõe a porta 8080 (padrão do Spring Boot)
EXPOSE 8080

# Copia o .jar que foi criado no estágio de build
COPY --from=build /app/target/*.jar app.jar

# Define o comando para executar a aplicação quando o contêiner iniciar
ENTRYPOINT ["java","-jar","/app.jar"]