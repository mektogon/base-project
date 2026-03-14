dependencies {
    implementation("org.springframework.boot:spring-boot-configuration-processor")
    implementation("org.springframework.boot:spring-boot-autoconfigure")
    implementation("org.springframework.kafka:spring-kafka")

    implementation("org.simplejavamail:simple-java-mail:8.12.6")

    api("org.testcontainers:kafka:1.21.4")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks {
    jar {
        enabled = true //Enable generation jar-plain.
    }

    bootJar {
        enabled = false //Disable generate executable jar.
    }
}