buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath(platform("org.jmolecules:jmolecules-bom:2023.3.1"))
        classpath("org.jmolecules.integrations:jmolecules-bytebuddy")
    }
}
plugins {
    java
    id("org.springframework.boot") version "3.4.4"
    id("io.spring.dependency-management") version "1.1.7"
    id("net.bytebuddy.byte-buddy-gradle-plugin") version "1.17.5"
}

group = "gae.piaz"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(23)
    }
}

repositories {
    mavenCentral()
}

extra["springModulithVersion"] = "1.3.4"
extra["jMoleculesVersion"] = "2023.3.1"

dependencies {
    implementation("org.jmolecules.integrations:jmolecules-starter-ddd")
    implementation("org.jmolecules:jmolecules-events")
    implementation("org.jmolecules:jmolecules-cqrs-architecture")
    implementation("org.jmolecules.integrations:jmolecules-jpa")
    compileOnly("org.jmolecules.integrations:jmolecules-bytebuddy-nodep")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.modulith:spring-modulith-starter-core")
    implementation("org.springframework.modulith:spring-modulith-events-api")
    implementation("org.springframework.modulith:spring-modulith-starter-jpa")

    implementation("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")

    runtimeOnly("com.h2database:h2")
    runtimeOnly("org.postgresql:postgresql")
    runtimeOnly("org.springframework.boot:spring-boot-docker-compose")

    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-database-postgresql")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.modulith:spring-modulith-starter-test")
    testImplementation("org.awaitility:awaitility:4.2.0")

    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.modulith:spring-modulith-bom:${property("springModulithVersion")}")
        mavenBom("org.jmolecules:jmolecules-bom:${property("jMoleculesVersion")}")
    }
}

byteBuddy {
    transformation {
        plugin = org.jmolecules.bytebuddy.JMoleculesPlugin::class.java
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
