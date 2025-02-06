import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("com.android.library")
}

val ktorVersion: String by project
val logbackVersion: String by project
val junitVersion: String by project

val jdkVersion = JavaVersion.VERSION_17

kotlin {
    explicitApi()

    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = jdkVersion.toString()
        }
        testRuns.configureEach {
            executionTask.configure {
                useJUnitPlatform()
            }
        }
    }

    androidTarget {
        publishLibraryVariants("release")
        compilerOptions {
            jvmTarget.set(JvmTarget.fromTarget(jdkVersion.toString()))
        }
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    js()

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
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.1")
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
        jsMain.dependencies {
            implementation("io.ktor:ktor-client-js:$ktorVersion")
        }
    }
}

android {
    namespace = "io.github.zenhelix"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
    }
}