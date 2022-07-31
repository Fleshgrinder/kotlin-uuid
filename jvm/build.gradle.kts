plugins {
    `java-library`
    id("me.champeau.jmh") version "0.6.6"
    `maven-publish`
}

dependencies {
    api("org.jetbrains:annotations:23.0.0")
    api("com.fasterxml.jackson.core:jackson-annotations:2.10.0")

    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
    testImplementation(platform("com.fasterxml.jackson:jackson-bom:2.13.3"))
    testImplementation("com.fasterxml.jackson.core:jackson-databind")
}

tasks {
    test {
        useJUnitPlatform()
        systemProperties(
            "junit.jupiter.testinstance.lifecycle.default" to "per_class",
            "junit.jupiter.execution.parallel.enabled" to "true",
            "junit.jupiter.execution.parallel.mode.default" to "concurrent",
        )
    }
}
