plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

val kotlinxSerializationVersion: String by project

val jdkVersion = JavaVersion.VERSION_17

kotlin {
    explicitApi()

    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = jdkVersion.toString()
        }
    }

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