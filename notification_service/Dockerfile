FROM openjdk:13
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} notification_service.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "notification_service.jar"]