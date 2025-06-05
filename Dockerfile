FROM eclipse-temurin:17-jdk-alpine
LABEL maintainer="pisethath@gmail.com"
COPY target/book-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]