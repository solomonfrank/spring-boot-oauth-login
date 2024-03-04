FROM maven:3.8.3-openjdk-17 AS build

FROM openjdk:17-jdk

WORKdir /app

RUN mvn clean package

COPY target/*.jar /app/springOAuth.jar


EXPOSE 8080

CMD ["java", "-jar", "springOAuth.jar"]