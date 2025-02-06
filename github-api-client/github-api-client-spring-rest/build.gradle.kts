plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    `java-library`
}

val springBootVersion: String by project
val kotlinxSerializationVersion: String by project
val junitVersion: String by project

dependencies {
    api(rootProject.projects.githubApi)
    implementation(platform("org.springframework.boot:spring-boot-dependencies:$springBootVersion"))
    implementation("org.springframework:spring-web")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinxSerializationVersion")

    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
    testImplementation("org.springframework:spring-test")
}

kotlin {
    explicitApi()
}

tasks.test {
    useJUnitPlatform()
}
