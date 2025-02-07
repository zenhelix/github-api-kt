plugins {
    id("io.github.zenhelix.kmm-library")
    kotlin("plugin.serialization")
}

val ktorVersion: String by project
val logbackVersion: String by project
val junitVersion: String by project
val kotlinxCoroutinesVersion: String by project

kotlin {
    jvm {
        testRuns.configureEach {
            executionTask.configure {
                useJUnitPlatform()
            }
        }
    }

    androidTarget {
        publishLibraryVariants("release")
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(rootProject.projects.githubApi)
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
                implementation("io.ktor:ktor-client-logging:$ktorVersion")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation("io.ktor:ktor-client-mock:$ktorVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$kotlinxCoroutinesVersion")
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-cio:$ktorVersion")
                implementation("ch.qos.logback:logback-classic:$logbackVersion")
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit5"))
                implementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
                runtimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
            }
        }

        androidMain.dependencies {
            implementation("io.ktor:ktor-client-cio:$ktorVersion")
        }
        iosMain.dependencies {
            implementation("io.ktor:ktor-client-cio:$ktorVersion")
        }
        iosTest.dependencies {
            implementation(kotlin("test"))
        }
        jsMain.dependencies {
            implementation("io.ktor:ktor-client-js:$ktorVersion")
        }
        jsTest.dependencies {
            implementation(kotlin("test-js"))
        }
        wasmJsTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}

android {
    namespace = "io.github.zenhelix"
}