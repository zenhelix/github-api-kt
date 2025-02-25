plugins {
    id("io.github.zenhelix.kotlin-jvm-library")
    id("io.github.zenhelix.spring-boot-autoconfigure-library")
}

val springBootVersion: String by project
val springKtVersion: String by project

dependencies {
    implementation(platform("org.springframework.boot:spring-boot-dependencies:$springBootVersion"))
    add("kapt", platform("org.springframework.boot:spring-boot-dependencies:$springBootVersion"))
    implementation(platform("io.github.zenhelix:spring-kt-platform-bom:$springKtVersion"))

    compileOnly(rootProject.projects.githubApiClient.githubApiClientSpringWebflux)

    implementation("io.github.zenhelix:spring-web-autoconfigure")

    compileOnly("io.github.zenhelix:reactor-netty-http-client-support")
    compileOnly("io.projectreactor.netty:reactor-netty")

    compileOnly("jakarta.validation:jakarta.validation-api")
    compileOnly("org.hibernate.validator:hibernate-validator")
}
