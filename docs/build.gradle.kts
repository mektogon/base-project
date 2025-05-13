import org.asciidoctor.gradle.jvm.AsciidoctorTask

plugins {
    id("org.asciidoctor.jvm.convert") version "4.0.4"
}

tasks {

    val buildAdminGuide = register("buildAdminGuide", AsciidoctorTask::class) {
        val currentSourceDir = "${rootDir}/docs/admin-guide"
        setSourceDir(file(currentSourceDir))
        setOutputDir(file(file("${rootDir}/docs/build/docs")))
        sources(delegateClosureOf<PatternSet> {
            include("admin-guide.adoc")
        })
        resources(delegateClosureOf<CopySpec> {
            from("${currentSourceDir}/img") {
                include("*.png", "*.jpg", "*.gif")
            }
            into("./img")
        })
    }

    val buildBackDevGuide = register("buildBackDevGuide", AsciidoctorTask::class) {
        val currentSourceDir = "${rootDir}/docs/back-dev-guide"
        setSourceDir(file(currentSourceDir))
        setOutputDir(file(file("${rootDir}/docs/build/docs")))
        sources(delegateClosureOf<PatternSet> {
            include("back-dev-guide.adoc")
        })
        resources(delegateClosureOf<CopySpec> {
            from("${currentSourceDir}/img") {
                include("*.png", "*.jpg", "*.gif")
            }
            into("./img")
        })
    }

    val buildFrontDevGuide = register("buildFrontDevGuide", AsciidoctorTask::class) {
        val currentSourceDir = "${rootDir}/docs/front-dev-guide"
        setSourceDir(file(currentSourceDir))
        setOutputDir(file(file("${rootDir}/docs/build/docs")))
        sources(delegateClosureOf<PatternSet> {
            include("front-dev-guide.adoc")
        })
        resources(delegateClosureOf<CopySpec> {
            from("${currentSourceDir}/img") {
                include("*.png", "*.jpg", "*.gif")
            }
            into("./img")
        })
    }

    build {
        //https://docs.asciidoctor.org/gradle-plugin/latest/common-task-configuration
        dependsOn(buildAdminGuide, buildBackDevGuide, buildFrontDevGuide)
    }
}
