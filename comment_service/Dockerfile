FROM openjdk:13
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} comment_service.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "comment_service.jar"]