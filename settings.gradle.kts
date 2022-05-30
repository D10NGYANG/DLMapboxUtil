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
        jcenter()
        maven("https://jitpack.io")
        maven {
            setUrl("https://api.mapbox.com/downloads/v2/releases/maven")
            authentication {
                create<BasicAuthentication>("basic")
            }
            credentials {
                // Do not change the username below.
                // This should always be `mapbox` (not your username).
                username = "mapbox"
                // Use the secret token you stored in gradle.properties as the password
                password = "sk.eyJ1IjoiaXJpbTEwMCIsImEiOiJja2phdW92MWQxcnlhMnRueXpoYzZlbHNlIn0.TpnkoBa7iwi9GNN9j7ZjVg"
            }
        }
    }
}
rootProject.name = "DLMapboxUtil"
include("app")
include("mapbox")
