plugins {
    kotlin("plugin.serialization")
    id("io.github.zenhelix.kotlin-jvm-library")
    id("io.github.zenhelix.spring-library")
}

val springBootVersion: String by project
val kotlinxCoroutinesVersion: String by project
val kotlinxSerializationVersion: String by project
val junitVersion: String by project
val mockwebserverVersion = "4.12.0"

dependencies {
    api(rootProject.projects.githubApi)
    implementation(platform("org.springframework.boot:spring-boot-dependencies:$springBootVersion"))
    implementation("org.springframework:spring-webflux")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinxSerializationVersion")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxCoroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:$kotlinxCoroutinesVersion")

    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")

    testImplementation("org.springframework:spring-test")
    testImplementation("com.squareup.okhttp3:mockwebserver:$mockwebserverVersion")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$kotlinxCoroutinesVersion")
}

tasks.test {
    useJUnitPlatform()
}
