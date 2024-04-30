plugins {
    java
    id("org.springframework.boot") version "3.2.3"
    id("io.spring.dependency-management") version "1.1.4"
    id ("jacoco")
}

group = "com.example"
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
    implementation("org.springframework.boot:spring-boot-starter-web")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    //CACHE
    implementation("org.springframework.boot:spring-boot-starter-cache")
    //VALIDACIONES
    implementation("org.springframework.boot:spring-boot-starter-validation")
    //JPA
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    //XML_JSON
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml")
    //H2
    runtimeOnly("com.h2database:h2")
    //WEBSOCKET
    implementation("org.springframework.boot:spring-boot-starter-websocket")
    //SWAGGER
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")
    //MONGODB
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    //THYMELEAF
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    //SECURITY
    implementation("org.springframework.boot:spring-boot-starter-security")
    //JWT
    implementation("com.auth0:java-jwt:4.4.0")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.jacocoTestReport {
    dependsOn(tasks.test) // tests are required to run before generating the report
}

tasks.jacocoTestReport {
    reports {
        xml.required = false
        csv.required = false
        html.outputLocation = layout.buildDirectory.dir("jacocoHtml")
    }
}