val bds100MavenUsername: String by settings
val bds100MavenPassword: String by settings
val mapboxUsername: String by settings
val mapboxPassword: String by settings

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
        maven("https://raw.githubusercontent.com/D10NGYANG/maven-repo/main/repository")
        maven {
            setUrl("https://nexus.bds100.com/repository/maven-releases/")
            credentials {
                username = bds100MavenUsername
                password = bds100MavenPassword
            }
        }
        maven {
            setUrl("https://api.mapbox.com/downloads/v2/releases/maven")
            authentication {
                create<BasicAuthentication>("basic")
            }
            credentials {
                username = mapboxUsername
                password = mapboxPassword
            }
        }
    }
}
rootProject.name = "DLMapboxUtil"
include("app")
include("mapbox")
