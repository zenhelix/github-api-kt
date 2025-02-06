plugins {
    kotlin("jvm") apply false
    kotlin("multiplatform") apply false
    kotlin("plugin.serialization") apply false
    id("com.android.library") apply false
}

allprojects {
    group = "io.github.zenhelix"
}

subprojects {
    apply(plugin = "maven-publish")
}