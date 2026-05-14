FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY . .

RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

EXPOSE 8080

# Copier explicitement le JAR généré (adapte le nom si besoin)
CMD ["sh", "-c", "java -jar target/$(ls target/*.jar | head -n 1)"]