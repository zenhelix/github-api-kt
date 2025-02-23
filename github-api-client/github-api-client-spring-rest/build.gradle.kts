plugins {
    kotlin("plugin.serialization")
    id("io.github.zenhelix.kotlin-jvm-library")
    id("io.github.zenhelix.spring-library")
}

val springBootVersion: String by project
val kotlinxSerializationVersion: String by project
val junitVersion: String by project

dependencies {
    api(rootProject.projects.githubApi)

    implementation(platform("org.springframework.boot:spring-boot-dependencies:$springBootVersion"))

    api("org.springframework:spring-web")
    api("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinxSerializationVersion")

    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")

    testImplementation("org.springframework:spring-test")
}

tasks.test {
    useJUnitPlatform()
}
