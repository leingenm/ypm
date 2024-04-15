FROM openjdk:17-jdk-slim
MAINTAINER daverbk, RomanMager
COPY build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
