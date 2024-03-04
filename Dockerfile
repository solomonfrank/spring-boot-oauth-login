FROM openjdk:17-jdk

WORKdir /app

RUN  mvn clean package

COPY target/*.jar /app/springOAuth.jar


EXPOSE 8080

CMD ["java", "-jar", "springOAuth.jar"]