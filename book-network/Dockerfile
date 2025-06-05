# Etapa 1 – Build cu Maven
FROM maven:3.8.5-eclipse-temurin-17 AS builder
WORKDIR /build
COPY . .
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

# Etapa 2 – Imagine finală cu JAR
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
RUN apk add --no-cache curl
COPY --from=builder /build/target/book-network-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8088
ENTRYPOINT ["java", "-jar", "app.jar"]
