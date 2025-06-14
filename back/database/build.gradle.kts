import java.io.ByteArrayOutputStream

tasks {
    jar {
        //Исключаем, чтобы избежать конфликта выбора changelog.
        exclude("**/liquibase/**")
    }
}

sourceSets {
    main {
        resources {
            //Исключаем, чтобы избежать конфликта выбора changelog.
            exclude("**/liquibase/**")
        }
    }
}

dependencies {
    api("org.springframework.boot:spring-boot-starter-data-jpa")
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

    val basePathToFile = project.layout.projectDirectory.dir("src/main/resources/liquibase/changelog")
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

/**
 * Функция получения списка модулей, использующих определенный подмодуль.
 */
fun getModulesUsingSubmodule(submoduleName: String): List<String> {
    val result = mutableListOf<String>()
    rootProject.allprojects.forEach { project ->
        // Проверка всех конфигураций проекта на наличие зависимости с указанным именем
        project.configurations.forEach { configuration ->
            if (configuration.dependencies.any { dependency -> dependency.name == submoduleName }) {
                result.add(project.name)
            }
        }
    }
    return result
}

//Оставляю как артефакт разработки, в качестве альтернативного решения распространения resources между модулями.
tasks.register("copyLiquibaseResourcesToModule") {
    description = "Копирует ресурсы Liquibase из модуля 'database' во все модули, которые его используют."

    dependsOn(tasks.processResources)
    val buildSrcDir = layout.buildDirectory.dir("resources/main/liquibase")
    val srcAbsolutePath = buildSrcDir.get().asFile.absolutePath
    val rootProjectName = rootProject.name
    val targetResourcesPath = "/resources/main/liquibase"

    doFirst {
        val modulesUsingDatabase = getModulesUsingSubmodule(project.name)
        println("Выполняем копирование resources из '$srcAbsolutePath'")
        modulesUsingDatabase.forEach { moduleName ->

            val targetAbsolutePath: String = if (rootProjectName == moduleName) {
                "${rootProject.buildDir}${targetResourcesPath}"
            } else {
                "${rootProject.project(":$moduleName").buildDir}${targetResourcesPath}"
            }

            println("В модуль '$moduleName' каталог: $targetAbsolutePath")

            copy {
                from(srcAbsolutePath)
                into(targetAbsolutePath)
            }
        }
    }

    doLast {
        println("Выполняю очистку resources: '$srcAbsolutePath'!")
        delete(buildSrcDir)
    }
}