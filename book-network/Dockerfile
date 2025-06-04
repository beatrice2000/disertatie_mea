# Folosește o imagine oficială OpenJDK
FROM openjdk:17-jdk-slim

# Setează directorul de lucru
WORKDIR /app

# Copiază JAR-ul construit 
COPY target/*.jar app.jar

# Expune portul pe care Spring Boot rulează 
EXPOSE 8088

# Comandă de start
ENTRYPOINT ["java", "-jar", "app.jar"]
