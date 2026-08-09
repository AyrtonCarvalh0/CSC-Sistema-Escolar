# === Build ===
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
# Baixar dependências primeiro (cache do Docker)
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests -B

# === Runtime ===
FROM eclipse-temurin-21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Render injeta a variável PORT
EXPOSE ${PORT:-8080}

# JVM otimizada para 512MB (free tier do Render)
ENTRYPOINT ["java", "-Xms128m", "-Xmx384m", "-XX:+UseSerialGC", "-jar", "app.jar", "--spring.profiles.active=prod"]
