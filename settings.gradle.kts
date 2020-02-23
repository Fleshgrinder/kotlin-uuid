rootProject.name = "kotlin-uuid"

pluginManagement {
    val kotlinVersion = file(".kotlin-version").readText().trim()
    plugins {
        kotlin("multiplatform") version kotlinVersion
    }
}
