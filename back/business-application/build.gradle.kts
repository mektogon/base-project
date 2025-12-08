dependencies {
    implementation(project(":back:database"))

    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("io.micrometer:micrometer-registry-prometheus:1.13.5")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.6")

    testImplementation("io.rest-assured:rest-assured:5.5.5")
    //https://allurereport.org
    implementation("io.qameta.allure:allure-rest-assured:2.29.1")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:junit-jupiter")
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
            args(project.properties["args"]?.toString()?.split(",") as Iterable<String>)
        }
    }
}