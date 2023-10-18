FROM openjdk:17
LABEL authors="Vitor Furini"

ADD target/*.jar app.jar
ADD run.sh run.sh

RUN chmod +x run.sh

ENTRYPOINT ["java", "-jar", "/app.jar"]