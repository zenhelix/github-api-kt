import org.gradle.api.internal.tasks.JvmConstants.COMPILE_JAVA_TASK_NAME
import org.gradle.api.internal.tasks.JvmConstants.PROCESS_RESOURCES_TASK_NAME

plugins {
    kotlin("jvm")
    `java-library`
    kotlin("kapt")
    kotlin("plugin.spring")
}

val springBootVersion: String by project

dependencies {
    compileOnly(rootProject.projects.githubApiClient.githubApiClientSpringWebflux)

    implementation(platform("org.springframework.boot:spring-boot-dependencies:$springBootVersion"))

    implementation("org.springframework.boot:spring-boot-autoconfigure")

    kapt("org.springframework.boot:spring-boot-configuration-processor")
    kapt("org.springframework.boot:spring-boot-autoconfigure-processor")

    compileOnly("jakarta.validation:jakarta.validation-api")
    compileOnly("org.hibernate.validator:hibernate-validator:8.0.2.Final")
}

// see https://docs.spring.io/spring-boot/docs/current/reference/html/configuration-metadata.html#appendix.configuration-metadata.annotation-processor.configuring
tasks.named(COMPILE_JAVA_TASK_NAME) {
    inputs.files(tasks.named(PROCESS_RESOURCES_TASK_NAME))
}

kotlin {
    explicitApi()
}
