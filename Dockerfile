FROM openjdk:17-jdk
WORKDIR /app
COPY target/planning-0.0.1-SNAPSHOT.jar /app/planning.jar
EXPOSE 8080
CMD ["java", "-jar", "planning.jar"]