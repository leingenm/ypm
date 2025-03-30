FROM gradle:8-jdk17-jammy AS build
WORKDIR /app
COPY settings.gradle.kts ./
COPY build.gradle.kts ./
COPY ./src ./src
RUN gradle build --no-daemon -x test

FROM openjdk:17-jdk-slim
MAINTAINER daverbk, leingenm
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar

RUN useradd -m ypm-user
USER ypm-user

EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java -jar /app/app.jar"]
