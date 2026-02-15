dependencies {
    implementation(project(":back:database"))

    api("com.redis.testcontainers:testcontainers-redis:1.6.4")
    api("org.springframework.boot:spring-boot-starter-security")

    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.session:spring-session-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-web")
}

tasks {
    jar {
        enabled = true //Enable generation jar-plain.
    }

    bootJar {
        enabled = false //Disable generate executable jar.
    }
}