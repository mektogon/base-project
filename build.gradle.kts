allprojects {
    group = "ru.dorofeev"
    version = "01.000.00"

    repositories {
        mavenCentral()
    }

    dependencyLocking {
        lockFile.set(file("$projectDir/gradle/lockfiles/${rootProject.name}-${version}.lockfile"))
        lockAllConfigurations()
    }
}