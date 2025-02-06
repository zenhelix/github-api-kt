@file:Suppress("UnstableApiUsage")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "github-api-kt"

include("github-api")
project("github-api-client") {
    include("github-api-client-ktor")
    include("github-api-client-spring-rest")
    include("github-api-client-spring-webflux")
}
project("github-api-client-spring") {
    include("github-api-client-rest-autoconfigure")
    include("github-api-client-rest-starter")
    include("github-api-client-webflux-autoconfigure")
    include("github-api-client-webflux-starter")
}
project("github-api-platform") {
    include("github-api-platform-bom")
    include("github-api-platform-toml")
}

private fun Settings.project(
    baseProject: String, initializer: IncludeContext.() -> Unit = {}
): IncludeContext = IncludeContext(baseProject, this).apply(initializer)

private class IncludeContext(private val baseProject: String, private val delegate: Settings) {

    fun project(
        baseProject: String, initializer: IncludeContext.() -> Unit = {}
    ): IncludeContext = IncludeContext("${this.baseProject}:$baseProject", this.delegate).apply(initializer)

    fun include(vararg project: String) {
        project.forEach {
            delegate.include("$baseProject:$it")
        }
    }

}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google()
        mavenLocal()
    }
}

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenLocal()
    }

    val kotlinVersion: String by settings
    val androidVersion: String by settings

    plugins {
        id("org.jetbrains.kotlin.multiplatform") version kotlinVersion
        id("org.jetbrains.kotlin.jvm") version kotlinVersion
        id("org.jetbrains.kotlin.plugin.serialization") version kotlinVersion
        id("org.jetbrains.kotlin.kapt") version kotlinVersion
        id("org.jetbrains.kotlin.plugin.spring") version kotlinVersion

        id("com.android.library") version androidVersion
    }
}