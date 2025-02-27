FROM eclipse-temurin:17-alpine-3.20 AS builder

WORKDIR /app

COPY . .

RUN ./gradlew build -x test


FROM eclipse-temurin:17-jre-alpine-3.20

WORKDIR /app

COPY --from=builder /app/build/libs/mavis-*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
