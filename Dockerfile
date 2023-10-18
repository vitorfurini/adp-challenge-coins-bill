FROM openjdk:17
LABEL authors="Vitor Furini"

ADD target/invoice-api-0.0.1-SNAPSHOT.jar app.jar

ARG JAR_FILE=out/artifacts/invoice_api_jar/*.jar

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]