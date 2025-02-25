plugins {
    `java-library`
    id("io.github.zenhelix.kotlin-jvm-library") apply false
    id("io.github.zenhelix.spring-boot-starter") apply false
}

val springBootVersion: String by project
val springKtVersion: String by project

project("github-api-client-rest-starter") {
    apply(plugin = "io.github.zenhelix.kotlin-jvm-library")
    apply(plugin = "io.github.zenhelix.spring-boot-starter")

    dependencies {
        implementation(platform("org.springframework.boot:spring-boot-dependencies:$springBootVersion"))
        add("kapt", platform("org.springframework.boot:spring-boot-dependencies:$springBootVersion"))
        implementation(platform("io.github.zenhelix:spring-kt-platform-bom:$springKtVersion"))

        api(rootProject.projects.githubApiClientSpring.githubApiClientRestAutoconfigure)
        api(rootProject.projects.githubApiClient.githubApiClientSpringRest)

        implementation("io.github.zenhelix:apache-http-client-starter")

        testImplementation("org.springframework.boot:spring-boot-starter-web")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
    }
}

project("github-api-client-webflux-starter") {
    apply(plugin = "io.github.zenhelix.kotlin-jvm-library")
    apply(plugin = "io.github.zenhelix.spring-boot-starter")

    dependencies {
        implementation(platform("org.springframework.boot:spring-boot-dependencies:$springBootVersion"))
        add("kapt", platform("org.springframework.boot:spring-boot-dependencies:$springBootVersion"))
        implementation(platform("io.github.zenhelix:spring-kt-platform-bom:$springKtVersion"))

        api(rootProject.projects.githubApiClientSpring.githubApiClientWebfluxAutoconfigure)
        api(rootProject.projects.githubApiClient.githubApiClientSpringWebflux)

        implementation("io.github.zenhelix:reactor-netty-http-client-starter")

        testImplementation("org.springframework.boot:spring-boot-starter-web")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
    }
}
