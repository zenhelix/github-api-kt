plugins { `java-platform` }

val javaPlatformComponentName: String = "javaPlatform"

publishing {
    publications {
        create<MavenPublication>(project.name) {
            from(components.getByName(javaPlatformComponentName))
        }
    }
}

dependencies {
    constraints {
        rootProject.subprojects.filter { it.childProjects.isEmpty() }.filter { it.name.contains("github-api-platform-").not() }.forEach {
            api(it)
        }
    }
}
