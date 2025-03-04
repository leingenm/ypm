plugins {
    java
    id("org.springframework.boot") version "3.3.5"
    id("io.spring.dependency-management") version "1.1.6"
}

group = "xyz.ypmngr"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
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
    implementation(group = "org.springframework.boot", name = "spring-boot-starter-web")
    implementation(group = "org.springframework.boot", name = "spring-boot-starter-security")
    implementation(group = "org.springframework.boot", name = "spring-boot-starter-oauth2-resource-server")

    // Data Access
    implementation(group = "org.springframework.boot", name = "spring-boot-starter-data-jpa")
    implementation(group = "org.liquibase", name = "liquibase-core")
    runtimeOnly(group = "org.postgresql", name= "postgresql")

    // Development
    developmentOnly(group = "org.springframework.boot", name = "spring-boot-devtools")
    developmentOnly(group = "org.springframework.boot", name = "spring-boot-docker-compose")
    annotationProcessor(group = "org.springframework.boot", name = "spring-boot-configuration-processor")

    // YouTube Client
    implementation(group = "com.google.apis", name = "google-api-services-youtube", version = "v3-rev20241022-2.0.0")
    implementation(group = "com.google.api-client", name = "google-api-client", version = "2.7.0")
    implementation(group = "com.google.http-client", name = "google-http-client", version = "1.45.0")
    implementation(group = "com.google.code.gson", name = "gson", version = "2.11.0")

    // Lombok
    compileOnly(group = "org.projectlombok", name = "lombok")
    annotationProcessor(group = "org.projectlombok", name = "lombok")

    // Test
    testImplementation(group = "org.springframework.boot", name = "spring-boot-starter-test")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
