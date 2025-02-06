plugins {
    `java-library`
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "java-library")
}

val springBootVersion: String by project

project("github-api-client-rest-starter") {
    dependencies {
        implementation(platform("org.springframework.boot:spring-boot-dependencies:$springBootVersion"))
        api(rootProject.projects.githubApiClientSpring.githubApiClientRestAutoconfigure)
        api(rootProject.projects.githubApiClient.githubApiClientSpringRest)

        testImplementation("org.springframework.boot:spring-boot-starter-web")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
    }
}

project("github-api-client-webflux-starter") {
    dependencies {
        implementation(platform("org.springframework.boot:spring-boot-dependencies:$springBootVersion"))
        api(rootProject.projects.githubApiClientSpring.githubApiClientWebfluxAutoconfigure)
        api(rootProject.projects.githubApiClient.githubApiClientSpringWebflux)

        testImplementation("org.springframework.boot:spring-boot-starter-web")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
    }
}
