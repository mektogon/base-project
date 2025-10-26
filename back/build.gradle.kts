plugins {
    `java-library`
    id("org.springframework.boot") version "3.5.7"
    id("io.spring.dependency-management") version "1.1.7"
}

configure<JavaPluginExtension> {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(JavaVersion.VERSION_21.majorVersion.toInt()))
    }
}

tasks {
    jar {
        enabled = false;
    }

    bootJar {
        enabled = false;
    }

    bootRun {
        enabled = false;
    }
}

subprojects {
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "java-library")

    dependencies {
        api("org.slf4j:slf4j-api:2.0.16")

        api("org.apache.commons:commons-collections4:4.5.0")
        api("org.apache.commons:commons-lang3:3.17.0")

        compileOnly("org.projectlombok:lombok:1.18.34")
        annotationProcessor("org.projectlombok:lombok:1.18.34")

        api("org.mapstruct:mapstruct:1.6.3")
        annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")
        annotationProcessor("org.projectlombok:lombok-mapstruct-binding:0.2.0")

        testCompileOnly("org.projectlombok:lombok")
        testAnnotationProcessor("org.projectlombok:lombok")
    }

    tasks {
        test {
            useJUnitPlatform()
            testLogging {
                events("PASSED", "SKIPPED", "FAILED")
            }
        }
    }
}
