plugins {
    `java-library`
    id("io.github.zenhelix.kotlin-jvm-library") apply false
    id("io.github.zenhelix.spring-boot-starter") apply false
}

val springBootVersion: String by project

project("github-api-client-rest-starter") {
    apply(plugin = "io.github.zenhelix.kotlin-jvm-library")
    apply(plugin = "io.github.zenhelix.spring-boot-starter")

    dependencies {
        implementation(platform("org.springframework.boot:spring-boot-dependencies:$springBootVersion"))
        api(rootProject.projects.githubApiClientSpring.githubApiClientRestAutoconfigure)
        api(rootProject.projects.githubApiClient.githubApiClientSpringRest)

        testImplementation("org.springframework.boot:spring-boot-starter-web")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
    }
}

project("github-api-client-webflux-starter") {
    apply(plugin = "io.github.zenhelix.kotlin-jvm-library")
    apply(plugin = "io.github.zenhelix.spring-boot-starter")

    dependencies {
        implementation(platform("org.springframework.boot:spring-boot-dependencies:$springBootVersion"))
        api(rootProject.projects.githubApiClientSpring.githubApiClientWebfluxAutoconfigure)
        api(rootProject.projects.githubApiClient.githubApiClientSpringWebflux)

        testImplementation("org.springframework.boot:spring-boot-starter-web")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
    }
}
