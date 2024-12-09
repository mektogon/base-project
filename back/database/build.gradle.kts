import java.io.ByteArrayOutputStream

val copyLiquibaseResourcesToMainModule by tasks.registering(Copy::class) {
    description = "Задача, выполняющая копирование liquibase-каталога :database-модуля " +
            "в :back-модуль, для корректного запуска в Docker-контейнере."
    dependsOn(tasks.processResources)
    val buildSrcDir = layout.buildDirectory.dir("resources/main");

    from(buildSrcDir)
    project(":back").the<SourceSetContainer>()["main"].output.resourcesDir?.let { into(it) }

    doLast {
        delete(buildSrcDir)
    }
}

tasks {
    jar {
        dependsOn(copyLiquibaseResourcesToMainModule)
        enabled = true
    }

    bootJar {
        enabled = false //Disable generation jar-package;
    }

    assemble {
        dependsOn(copyLiquibaseResourcesToMainModule)
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.liquibase:liquibase-core")

    runtimeOnly("org.postgresql:postgresql")
}

tasks.register("createPatch") {
    description = "Задача, выполняющая создание структуры для написания SQL-патча."

    /**
     * Получение имени пользователя, которое хранится в .gitconfig
     *
     * В случае отсутствия или пустого значения выводится заглушка: "<AUTHOR NAME>"
     */
    fun getGitUserName(): String {
        val defaultAuthorText = "<AUTHOR NAME>"
        val outputStream = ByteArrayOutputStream()

        try {
            exec {
                executable = "git"
                args("config", "--get", "user.name")
                standardOutput = outputStream
            }

            return outputStream.toString().trimIndent().ifEmpty {
                println("Warn! The username is empty! Return the stub value: $defaultAuthorText")
                defaultAuthorText
            }
        } catch (e: Exception) {
            println("Error! Couldn't find git username! Return the stub value: $defaultAuthorText")
        }

        return defaultAuthorText
    }

    /**
     * Добавление записи в changelog.yaml в зависимости от наличия или отсутствия файла.
     *
     * Если файл отсутствует - добавляется базовый текст, иначе добавляется новая запись к существующему тексту.
     *
     * @param file файл, в который осуществляется запись
     * @param path относительный путь до changelog
     */
    fun dataWriter(file: File, path: String) {
        val defaultDataChangelog = """
        databaseChangeLog:
            - include:
                file: $path
                relativeToChangelogFile: true
    """.trimIndent()

        val defaultDataIncludePatch = """
    - include:
        file: $path
        relativeToChangelogFile: true     
    """.trimEnd()

        if (!file.exists()) {
            file.writeText(defaultDataChangelog)
        } else {
            file.writeText(file.readText().plus(defaultDataIncludePatch))
        }
    }

    if (!project.hasProperty("patchname")) {
        throw GradleException("The name of the patch is missing!")
    }

    val versionToCatalogLiquibase = if (project.hasProperty("patchversion")) {
        project.property("patchversion")
    } else {
        project.version
    }

    val basePathToFile = project.layout.projectDirectory.dir("src/main/resources/db/changelog")
    val changelogName = "changelog.yaml"
    val patchName = "${System.currentTimeMillis()}_${project.property("patchname")}.sql"
    val gitUserName = getGitUserName()

    var defaultDataScript = """
        --liquibase formatted sql
        --changeset $gitUserName:$patchName
    """.trimIndent()

    if (project.hasProperty("type")) {
        defaultDataScript = defaultDataScript.plus("\n--type ${project.property("type")}")
    }

    if (project.hasProperty("task")) {
        defaultDataScript = defaultDataScript.plus("\n--comment ${project.property("task")}")
    }

    outputs.files(
            "$basePathToFile/$changelogName", //Master-changelog
            "$basePathToFile/$versionToCatalogLiquibase/$changelogName", //Local-changelog
            "$basePathToFile/$versionToCatalogLiquibase/$patchName" //Patch-script
    )

    doFirst {
        outputs.files.forEach { file ->
            if (file.name.equals(patchName)) {
                file.writeText(defaultDataScript) //Writing patch-script
            } else {
                if (file.absolutePath.contains(versionToCatalogLiquibase.toString())) { //Writing local-changelog
                    dataWriter(file, patchName)
                } else { //Writing to the master-changelog
                    if (!file.exists()) { //First writing
                        dataWriter(file, "$versionToCatalogLiquibase/$changelogName")
                    }
                    if (file.exists() && !file.readText().contains(versionToCatalogLiquibase.toString())) {
                        dataWriter(file, "$versionToCatalogLiquibase/$changelogName")
                    }
                }
            }
        }
    }
}