import org.asciidoctor.gradle.jvm.AsciidoctorTask

plugins {
    id("org.asciidoctor.jvm.convert") version "4.0.4"
}

tasks {

    val buildAdminGuide = register("buildAdminGuide", AsciidoctorTask::class) {
        setSourceDir(file("${rootDir}/docs/admin-guide"))
        setOutputDir(file(file("${rootDir}/docs/build/docs")))
        sources(delegateClosureOf<PatternSet> {
            include("admin-guide.adoc")
        })
    }

    val buildBackDevGuide = register("buildBackDevGuide", AsciidoctorTask::class) {
        setSourceDir(file("${rootDir}/docs/back-dev-guide"))
        setOutputDir(file(file("${rootDir}/docs/build/docs")))
        sources(delegateClosureOf<PatternSet> {
            include("back-dev-guide.adoc")
        })
    }

    val buildFrontDevGuide = register("buildFrontDevGuide", AsciidoctorTask::class) {
        setSourceDir(file("${rootDir}/docs/front-dev-guide"))
        setOutputDir(file(file("${rootDir}/docs/build/docs")))
        sources(delegateClosureOf<PatternSet> {
            include("front-dev-guide.adoc")
        })
    }

    build {
        //https://docs.asciidoctor.org/gradle-plugin/latest/common-task-configuration
        dependsOn(buildAdminGuide, buildBackDevGuide, buildFrontDevGuide)
    }
}
