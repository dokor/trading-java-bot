# ---- Build stage ----
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# cache deps
COPY pom.xml .
RUN mvn -q -e -DskipTests dependency:go-offline

# build
COPY src ./src
RUN mvn -q -DskipTests package

# ---- Runtime stage ----
FROM eclipse-temurin:21-jre
WORKDIR /app

# (optionnel) utilisateur non-root
RUN useradd -m appuser
USER appuser

# Copie du jar (adapte le pattern si besoin)
COPY --from=build /app/target/*.jar /app/app.jar

EXPOSE 8080
ENV SERVER_PORT=8080

# JVM flags safe sur Raspberry
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75.0 -XX:+UseG1GC"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
