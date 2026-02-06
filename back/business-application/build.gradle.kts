dependencies {
    implementation(project(":back:database"))
    implementation(project(":back:security"))

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

tasks.register("downloadOpenApiSpecification") {
    description = "Скачивание спецификации OpenAPI из http://localhost:port/api-docs.yml"

    doLast {
        val targetDir = File("${rootDir}/openapi/base-project")
        if (!targetDir.exists()) {
            throw RuntimeException("Не удалось найти директорию: ${targetDir} для спецификации OpenAPI!")
        }

        val outputFile = File(targetDir, "business-application.yaml")

        logger.lifecycle("Работаем с директорией: $targetDir")
        logger.lifecycle("Файл будет сохранён как: $outputFile")
        val port = 8081

        val command = listOf(
            "curl",
            "-o", outputFile.absolutePath,
            "http://localhost:${port}/v3/api-docs.yaml"
        )

        val process = ProcessBuilder(*command.toTypedArray())
            .redirectErrorStream(true)
            .start()

        val exitCode = process.waitFor()

        (exitCode == 0).also { isSuccess ->
            if (isSuccess) {
                logger.lifecycle("Файл со спецификацией сохранён в: ${outputFile.absolutePath}")
            } else {
                throw RuntimeException("Не удалось скачать спецификацию OpenAPI! Проверьте запущено ли приложение на порте '${port}'!")
            }
        }
    }
}
