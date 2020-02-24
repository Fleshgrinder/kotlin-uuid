import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
plugins {
    kotlin("jvm")
    id("me.champeau.gradle.jmh") version "0.5.0"
}

jmh {
    failOnError = true
    forceGC = true
    jmhVersion = "1.23"
    System.getenv("JAVA_HOME")?.let { jvm = "$it/bin/java" }
}

dependencies {
    api(kotlin("stdlib-jdk8"))
    api(project(":"))

    jmh("org.openjdk.jmh:jmh-core:${jmh.jmhVersion}")
    jmh("org.openjdk.jmh:jmh-generator-annprocess:${jmh.jmhVersion}")
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        freeCompilerArgs = listOf("-progressive")
    }
}
