FROM openjdk:17-jdk

WORKdir /app

COPY  target/springOAuth-0.0.1-SNAPSHOT.jar /app/springOAuth.jar

EXPOSE 8080

CMD ["java", "-jar", "springOAuth.jar"]