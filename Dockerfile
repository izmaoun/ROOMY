FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY . .

RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

EXPOSE 8080

# Trouve et exécute le bon JAR
CMD ["sh", "-c", "java -jar target/reservation_hotels-*.jar"]