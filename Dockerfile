FROM openjdk:17-jdk

WORKdir /app

# COPY . .

# RUN mvn clean package

ARG JAR_FILE=target/*.jar

COPY target/*.jar /app/springOAuth.jar

ENV SPRING_PROFILES_ACTIVE=prod


EXPOSE 8080

CMD ["java", "-jar", "springOAuth.jar", ]