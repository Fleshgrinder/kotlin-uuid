@file:Suppress("UNUSED_VARIABLE")

import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinNativeLink
import org.jetbrains.kotlin.konan.target.HostManager

plugins {
    id("idea")
    kotlin("multiplatform") version "1.3.61"
}

allprojects {
    apply(plugin = "idea")
    idea {
        module {
            isDownloadJavadoc = false
            isDownloadSources = true
        }
    }

    repositories {
        jcenter()
    }
}

val ideaActive = System.getProperty("idea.active") == "true"

kotlin {
    // TODO Kotlin has support for many more targets but we do not know how to
    //   test them. Examples: wasm, linux other than x64, mingw x86

    js {
        browser()
        nodejs {
            testTask {
                debug = false
            }
        }
        compilations.all {
            kotlinOptions {
                moduleKind = "umd"
                sourceMap = true
            }
        }
    }

    jvm {
        compilations.all {
            kotlinOptions {
                System.getenv("KOTLIN_JAVA_HOME")?.let { jdkHome = it }
            }
        }
    }

    if (ideaActive) {
        when {
            HostManager.hostIsMac -> macosX64("native")
            HostManager.hostIsLinux -> linuxX64("native")
            HostManager.hostIsMingw -> mingwX64("native")
            else -> error("Cannot prepare native target for unknown host: ${HostManager.hostName}")
        }
    }

    macosX64()
    ios()
    tvos()
    watchos()

    linuxX64()

    mingwX64()

    targets.all {
        compilations.all {
            kotlinOptions {
                allWarningsAsErrors = true
            }
        }
    }

    sourceSets {
        all {
            languageSettings.progressiveMode = true
        }

        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(kotlin("stdlib"))
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
            }
        }

        val nonJvmMain by creating {
            dependsOn(commonMain)
        }
        val nonJvmTest by creating {
            dependsOn(commonTest)
        }

        val jsMain by getting {
            dependsOn(nonJvmMain)
            dependencies {
                implementation(kotlin("stdlib-js"))
            }
        }
        val jsTest by getting {
            dependsOn(nonJvmTest)
            dependencies {
                implementation(kotlin("test-js"))
            }
        }

        val nativeMain = maybeCreate("nativeMain")
        nativeMain.dependsOn(nonJvmMain)
        val nativeTest = maybeCreate("nativeTest")
        nativeTest.dependsOn(nonJvmTest)

        targets.forEach {
            if (it is KotlinNativeTarget) {
                getByName("${it.name}Main") {
                    dependsOn(if (ideaActive) nonJvmMain else nativeMain)
                }
                getByName("${it.name}Test") {
                    dependsOn(if (ideaActive) nonJvmTest else nativeTest)
                }
            }
        }
    }
}

tasks {
    val jvmTest by getting(Test::class) {
        System.getenv("JAVA_TEST_HOME")?.let { executable = "$it/bin/java" }
    }

    val jvmMatrixTest by registering {
        doLast {
            val versionPattern = Regex("JAVA_HOME_[1-9][0-9]*\\.[1-9][0-9]*\\.[1-9][0-9]*(-ea)?_x64")
            System.getenv().filterKeys { it matches versionPattern }.forEach { (javaVersion, javaHome) ->
                println(javaVersion)
                exec {
                    environment("JAVA_TEST_HOME", javaHome)
                    commandLine("./gradlew", "jvmTest", "--stacktrace")
                }
            }
        }
    }

    if (HostManager.hostIsMac) {
        val linkDebugTestIosX64 by getting(KotlinNativeLink::class)
        val iosTest by registering(Exec::class) {
            group = "verification"
            description = "Run iOS tests with a simulator."
            dependsOn(linkDebugTestIosX64)
            commandLine("xcrun", "simctl", "spawn", "--standalone", "iPhone 8", linkDebugTestIosX64.outputFile.get())
        }

        val linkDebugTestTvosX64 by getting(KotlinNativeLink::class)
        val tvosTest by registering(Exec::class) {
            group = "verification"
            description = "Run tvOS tests with a simulator."
            dependsOn(linkDebugTestTvosX64)
            commandLine("xcrun", "simctl", "spawn", "--standalone", "Apple TV", linkDebugTestTvosX64.outputFile.get())
        }

        val linkDebugTestWatchosX86 by getting(KotlinNativeLink::class)
        val watchosTest by registering(Exec::class) {
            group = "verification"
            description = "Run watchOS tests with a simulator."
            dependsOn(linkDebugTestWatchosX86)
            commandLine("xcrun", "simctl", "spawn", "--standalone", "Apple Watch Series 4 - 40mm", linkDebugTestWatchosX86.outputFile.get())
        }
    }
}
