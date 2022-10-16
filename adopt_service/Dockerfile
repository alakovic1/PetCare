FROM openjdk:13
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} adopt_service.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "adopt_service.jar"]