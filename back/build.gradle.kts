plugins {
    `java-library`
    id("org.springframework.boot") version "3.4.5"
    id("io.spring.dependency-management") version "1.1.7"
}

val lombokVersion = "1.18.34"

subprojects {
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "java-library")

    java {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    tasks.jar {
        enabled = false //Disable generation plain-file for jar-package
    }

    dependencies {
        api("org.slf4j:slf4j-api:2.0.16")

        api("org.apache.commons:commons-collections4:4.5.0")
        api("org.apache.commons:commons-lang3:3.17.0")

        compileOnly("org.projectlombok:lombok:$lombokVersion")
        annotationProcessor("org.projectlombok:lombok:$lombokVersion")

        api("org.mapstruct:mapstruct:1.6.3")
        annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")
        annotationProcessor("org.projectlombok:lombok-mapstruct-binding:0.2.0")

        testCompileOnly("org.projectlombok:lombok:$lombokVersion")
        testAnnotationProcessor("org.projectlombok:lombok:$lombokVersion")
    }
}

dependencies {
    implementation(project(":back:database"))

    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("io.micrometer:micrometer-registry-prometheus:1.13.5")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.6")

    compileOnly("org.projectlombok:lombok:$lombokVersion")
    annotationProcessor("org.projectlombok:lombok:$lombokVersion")

    api("org.mapstruct:mapstruct:1.6.3")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")
    annotationProcessor("org.projectlombok:lombok-mapstruct-binding:0.2.0")

    testCompileOnly("org.projectlombok:lombok:$lombokVersion")
    testAnnotationProcessor("org.projectlombok:lombok:$lombokVersion")
    testImplementation("io.rest-assured:rest-assured:5.5.5")
    //https://allurereport.org
    implementation("io.qameta.allure:allure-rest-assured:2.29.1")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
}

sourceSets {
    main {
        resources {
            srcDirs("${rootDir}/back/database/src/main/resources")
        }
    }
    test {
        resources {
            srcDirs("${rootDir}/back/database/src/test/resources")
        }
    }
}

tasks {
    jar {
        enabled = false //Disable generation plain-file for jar-package
    }

    bootRun {
        if (project.hasProperty("args")) { //Many arguments for bootRun
            args(project.properties["args"]?.toString()?.split(","))
        }
    }

    withType<Test> {
        useJUnitPlatform()
    }
}