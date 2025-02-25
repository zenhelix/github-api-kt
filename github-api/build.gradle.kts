plugins {
    id("io.github.zenhelix.kmm-library")
    kotlin("plugin.serialization")
}

val kotlinxSerializationVersion: String by project

kotlin {

    sourceSets {
        val commonMain by getting {
            dependencies {
                compileOnly("org.jetbrains.kotlinx:kotlinx-serialization-core:$kotlinxSerializationVersion")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

    }
}

android {
    namespace = "io.github.zenhelix"
}