plugins {
    java
    id("org.springframework.boot") version "3.2.3"
    id("io.spring.dependency-management") version "1.1.4"
}

group = "com.ypm"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    // Spring Boot - Development
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    // developmentOnly("org.springframework.boot:spring-boot-docker-compose")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    // Database
    runtimeOnly("com.h2database:h2")
    runtimeOnly("com.mysql:mysql-connector-j")

    // YouTube Client
    implementation("com.google.apis:google-api-services-youtube:v3-rev20240310-2.0.0")
    implementation("com.google.api-client:google-api-client:2.4.0")
    // implementation("com.google.api-client:google-api-client-jackson2:1.28.1")
    implementation("com.google.http-client:google-http-client:1.44.1")
    implementation("com.google.oauth-client:google-oauth-client-jetty:1.35.0")
    implementation("com.google.code.gson:gson:2.10.1")

    // Lombok
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
