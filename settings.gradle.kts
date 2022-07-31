@file:Suppress("UnstableApiUsage")

rootProject.name = "kotlin-uuid"

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(
//    "jmh",
    "jvm",
//    "jvm-panama",
//    "jvm-valhalla",
    "kotlin",
)
