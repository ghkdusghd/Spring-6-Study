rootProject.name = "study"

include("apps:spring-security-ex1")
include("apps:spring-security-ex2")

pluginManagement {
    val springBootVersion: String by settings
    val springDependencyManagementVersion: String by settings

    resolutionStrategy {
        eachPlugin {
            when(requested.id.id) {
                "org.springframework.boot" -> useVersion(springBootVersion)
                "io.spring.dependency-management" -> useVersion(springDependencyManagementVersion)
            }
        }
    }
}

include("apps:spring-security-ex2")
