plugins {
    id("io.github.zenhelix.kotlin-jvm-library")
    id("io.github.zenhelix.spring-boot-autoconfigure-library")
}

val springBootVersion: String by project
val hibernateValidatorVersion = "8.0.2.Final"

dependencies {
    compileOnly(rootProject.projects.githubApiClient.githubApiClientSpringRest)

    implementation(platform("org.springframework.boot:spring-boot-dependencies:$springBootVersion"))
    kapt(platform("org.springframework.boot:spring-boot-dependencies:$springBootVersion"))

    compileOnly("jakarta.validation:jakarta.validation-api")
    compileOnly("org.hibernate.validator:hibernate-validator:$hibernateValidatorVersion")
}
