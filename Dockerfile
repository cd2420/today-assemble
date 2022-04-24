FROM adoptopenjdk/openjdk11
#CMD ["./mvnw", "clean", "package"]
#ARG JAR_FILE_PATH=target/*.jar
#COPY ${JAR_FILE_PATH} app.jar
#ENTRYPOINT ["java", "-jar", "app.jar"]
VOLUME /tmp
COPY target/today-assemble-0.0.1-SNAPSHOT.jar today-assemble.jar
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "today-assemble.jar"]
