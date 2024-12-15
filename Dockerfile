FROM amazoncorretto:21
COPY target/catsgram-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]