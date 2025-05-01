allprojects {
    group = "ru.dorofeev"
    version = "01.000.00"

    repositories {
        mavenCentral()
    }

    dependencyLocking {
        lockFile.set(file("$rootDir/gradle/lockfiles/${project.name}-${version}.lockfile"))
        lockAllConfigurations()
    }

    tasks.register("resolveAndLockAll") {
        notCompatibleWithConfigurationCache("Filters configurations at execution time")
        doFirst {
            require(gradle.startParameter.isWriteDependencyLocks) {
                "$path must be run from the command line with the `--write-locks` flag"
            }
        }
        doLast {
            configurations.filter {
                it.isCanBeResolved
            }.forEach { it.resolve() }
        }
    }
}