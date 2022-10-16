FROM openjdk:13
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} api_gateway.jar
EXPOSE 8088
ENTRYPOINT ["java", "-jar", "api_gateway.jar"]