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
  developmentOnly("org.springframework.boot:spring-boot-devtools")

  // YouTube Client
  implementation("com.google.apis:google-api-services-youtube:v3-rev20240310-2.0.0")
  implementation("com.google.api-client:google-api-client:2.3.0")
  implementation("com.google.http-client:google-http-client:1.44.1")
  implementation("com.google.oauth-client:google-oauth-client-jetty:1.35.0")
  implementation("com.google.api-client:google-api-client-jackson2:1.28.1")

  // Lombok
  compileOnly("org.projectlombok:lombok")
  annotationProcessor("org.projectlombok:lombok")

  // Test
  testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
  useJUnitPlatform()
}
