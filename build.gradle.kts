import org.jetbrains.kotlin.konan.target.HostManager
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTargetPreset

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

kotlin {
    val ideaActive = System.getProperty("idea.active") == "true"

    js {
        browser()
        nodejs {
            testTask {
                debug = false
            }
        }
    }

    jvm()

    if (ideaActive) {
        when {
            HostManager.hostIsMac -> macosX64("native")
            HostManager.hostIsLinux -> linuxX64("native")
            HostManager.hostIsMingw -> mingwX64("native")
            else -> error("Cannot prepare native target for unknown host: ${HostManager.hostName}")
        }
    }

    val nativeTargets = mutableSetOf<String>()
    presets.withType<KotlinNativeTargetPreset>().all {
        nativeTargets.add(name)
        targetFromPreset(this)
    }

    targets.all {
        compilations.all {
            kotlinOptions {
                allWarningsAsErrors = true
            }
        }
    }

    @Suppress("UNUSED_VARIABLE")
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

        val nativeMain = maybeCreate("native")
        nativeMain.dependsOn(nonJvmMain)
        val nativeTest = maybeCreate("native")
        nativeTest.dependsOn(nonJvmTest)

        nativeTargets.forEach {
            targets.getByName(it) {
                getByName("${it}Main") {
                    dependsOn(if (ideaActive) nonJvmMain else nativeMain)
                }
                getByName("${it}Test") {
                    dependsOn(if (ideaActive) nonJvmTest else nativeTest)
                }
            }
        }
    }
}
