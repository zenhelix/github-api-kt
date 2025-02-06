plugins {
    id("io.github.zenhelix.kotlin-jvm-library")
    id("io.github.zenhelix.spring-boot-autoconfigure-library")
}

val springBootVersion: String by project

dependencies {
    compileOnly(rootProject.projects.githubApiClient.githubApiClientSpringWebflux)

    implementation(platform("org.springframework.boot:spring-boot-dependencies:$springBootVersion"))
    kapt(platform("org.springframework.boot:spring-boot-dependencies:$springBootVersion"))

    compileOnly("jakarta.validation:jakarta.validation-api")
    compileOnly("org.hibernate.validator:hibernate-validator:8.0.2.Final")
}
