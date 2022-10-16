FROM openjdk:13
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} pet_category_service.jar
EXPOSE 8084
ENTRYPOINT ["java", "-jar", "pet_category_service.jar"]