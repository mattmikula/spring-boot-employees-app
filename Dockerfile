FROM openjdk:8-jdk-alpine

COPY /target/*.jar app.jar
ADD ./run.sh run.sh

ENTRYPOINT ["/run.sh"]