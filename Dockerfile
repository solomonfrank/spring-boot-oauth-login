FROM openjdk:17-jdk

WORKdir /app

RUN mkdir target

RUN  mvn clean package

RUN  ls

COPY  target/springOAuth.jar /app/springOAuth.jar

EXPOSE 8080

CMD ["java", "-jar", "springOAuth.jar"]