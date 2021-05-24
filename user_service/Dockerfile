FROM openjdk:13
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} user_service.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "user_service.jar"]