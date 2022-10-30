FROM adoptopenjdk/openjdk11
CMD ["./mvnw", "clean", "package"]
VOLUME /tmp
COPY target/today-assemble-1.0.jar today-assemble.jar
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "today-assemble.jar"]
