FROM openjdk:13
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} config_server.jar
EXPOSE 8888
ENTRYPOINT ["java", "-jar", "config_server.jar"]