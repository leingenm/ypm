FROM gradle:8.8-jdk17 AS builder
WORKDIR /app
COPY . .
RUN gradle clean build -x test

FROM amazoncorretto:17-alpine AS extractor
WORKDIR /extracted
COPY --from=builder /app/build/libs/*.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract

FROM amazoncorretto:17-alpine
WORKDIR application
COPY --from=extractor /extracted/dependencies/ ./
COPY --from=extractor /extracted/spring-boot-loader/ ./
COPY --from=extractor /extracted/snapshot-dependencies/ ./
COPY --from=extractor /extracted/application/ ./

EXPOSE 8080
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
