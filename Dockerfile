# Estágio 1: Build da aplicação
# Usamos a imagem completa do JDK da Eclipse Temurin
FROM eclipse-temurin:17-jdk-jammy AS build

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./

RUN ./mvnw dependency:go-offline

COPY src ./src

RUN ./mvnw package -DskipTests


# Estágio 2: Execução da aplicação
# Usamos a imagem leve do JRE da Eclipse Temurin
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

COPY --from=build /app/target/literalura-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]