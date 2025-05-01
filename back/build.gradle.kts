plugins {
    `java-library`
    id("org.springframework.boot") version "3.4.5"
    id("io.spring.dependency-management") version "1.1.7"
}

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
        api("org.projectlombok:lombok:1.18.34")
        api("org.mapstruct:mapstruct:1.6.3")
        api("org.mapstruct:mapstruct-processor:1.6.3")

        annotationProcessor("org.projectlombok:lombok:1.18.34")
    }
}

dependencies {
    implementation(project(":back:database"))

    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("io.micrometer:micrometer-registry-prometheus:1.13.5")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.6")

    annotationProcessor("org.projectlombok:lombok")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
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
            srcDirs("${rootDir}/back/database/src/main/resources")
        }
    }
}

tasks.jar {
    enabled = false //Disable generation plain-file for jar-package
}

tasks.bootRun {
    if (project.hasProperty("args")) { //Many arguments for bootRun
        args(project.properties["args"]?.toString()?.split(","))
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}