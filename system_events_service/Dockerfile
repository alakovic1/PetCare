FROM openjdk:13
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} system_events_service.jar
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "system_events_service.jar"]