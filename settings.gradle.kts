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

    val zenhelixGradleVersion: String by settings

    versionCatalogs {
        create("zenhelixPlugins") {
            from("io.github.zenhelix:gradle-magic-wands-catalog:$zenhelixGradleVersion")
        }
    }
}

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenLocal()
    }

    val zenhelixGradleVersion: String by settings
    val kotlinVersion: String by settings
    val mavenCentralPublishVersion: String by settings
    val atomicFuVersion: String by settings

    plugins {
        id("org.jetbrains.kotlin.plugin.serialization") version kotlinVersion
        id("org.jetbrains.kotlinx.atomicfu") version atomicFuVersion

        id("io.github.zenhelix.maven-central-publish") version mavenCentralPublishVersion

        id("io.github.zenhelix.kmm-library") version zenhelixGradleVersion
        id("io.github.zenhelix.kotlin-jvm-library") version zenhelixGradleVersion
        id("io.github.zenhelix.spring-library") version zenhelixGradleVersion
        id("io.github.zenhelix.spring-boot-autoconfigure-library") version zenhelixGradleVersion
        id("io.github.zenhelix.spring-boot-starter") version zenhelixGradleVersion
    }
}