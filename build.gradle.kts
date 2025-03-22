plugins {
    java
    id("org.springframework.boot") version "3.3.5"
    id("io.spring.dependency-management") version "1.1.6"
    id("org.openapi.generator") version "7.12.0"
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
    implementation(group = "org.springframework.boot", name = "spring-boot-configuration-processor")
    implementation(group = "org.springframework.boot", name = "spring-boot-starter-oauth2-resource-server")
    developmentOnly(group = "org.springframework.boot", name = "spring-boot-devtools")

    // Swagger
    implementation(group = "org.springdoc", name = "springdoc-openapi-starter-webmvc-ui", version = "2.6.0")

    // Util
    implementation(group = "org.apache.commons", name = "commons-csv", version = "1.14.0")

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

tasks {
    withType<Test> {
        useJUnitPlatform()
    }
    compileJava {
        dependsOn(openApiGenerate)
    }
}

val oasResourcesDir = "$projectDir/src/main/resources/static/oas"
val buildDir = layout.buildDirectory.get()
openApiGenerate {
    generatorName.set("spring")
    inputSpec.set("$oasResourcesDir/ypm.yaml")
    outputDir.set("$buildDir/generated")
    apiPackage.set("xyz.ypmngr.api")
    modelPackage.set("xyz.ypmngr.model")
    library.set("spring-boot")
    configOptions.set(
        mapOf(
            "useSpringBoot3" to "true",
            "useSwaggerUI" to "true",
            "interfaceOnly" to "true",
            "skipDefaultInterface" to "true",
            "openApiNullable" to "false",
        )
    )
}

sourceSets {
    main {
        java.srcDir("$buildDir/generated/src/main/java")
    }
}
