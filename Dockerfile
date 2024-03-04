FROM openjdk:17-jdk

WORKdir /app

COPY target/*.jar /app/springOAuth.jar


EXPOSE 8080

CMD ["java", "-jar", "springOAuth.jar"]