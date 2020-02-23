import org.gradle.internal.os.OperatingSystem
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

    if (ideaActive) {
        val os = OperatingSystem.current()
        when {
            os.isMacOsX -> macosX64("native")
            os.isLinux -> linuxX64("native")
            os.isWindows -> mingwX64("native")
            else -> error("Cannot prepare native target for unknown OS '$os'")
        }
    }

    js {
        if (!ideaActive) browser()
        nodejs()
    }
    jvm()

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
        }
        val jsTest by getting {
            dependsOn(nonJvmTest)
        }

        val nativeMain = maybeCreate("nativeMain")
        nativeMain.dependsOn(nonJvmMain)
        val nativeTest = maybeCreate("nativeTest")
        nativeTest.dependsOn(nonJvmTest)

        nativeTargets.forEach {
            targets.getByName(it) {
                getByName("${it}Main") {
                    dependsOn(nativeMain)
                }
                getByName("${it}Test") {
                    dependsOn(nativeTest)
                }
            }
        }
    }
}
