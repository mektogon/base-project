plugins {
    java
    id("org.springframework.boot") version "3.2.3"
    id("io.spring.dependency-management") version "1.1.4"
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
        implementation("org.slf4j:slf4j-api:2.0.12")

        compileOnly("org.projectlombok:lombok")

        annotationProcessor("org.projectlombok:lombok")
    }
}

dependencies {
    implementation(project(":back:database"))
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("io.micrometer:micrometer-registry-prometheus:1.13.5")
    implementation("io.springfox:springfox-swagger-ui:3.0.0")


    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
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