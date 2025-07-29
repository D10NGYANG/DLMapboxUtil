val bds100MavenUsername: String by project
val bds100MavenPassword: String by project

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    id("maven-publish")
}

group = "com.github.D10NGYANG"
version = "1.4.0"

android {
    namespace = "com.d10ng.mapbox"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlin {
        jvmToolchain(8)
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

ksp {
    arg("compose-destinations.codeGenPackageName", "com.d10ng.mapbox") // replace package name!
}

dependencies {
    // 单元测试（可选）
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.espresso)

    // Lifecycle
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // kotlinx.serialization
    implementation(libs.kotlinx.serialization.json)

    // startup
    implementation(libs.androidx.startup.runtime)

    // MapBox 地图
    api(libs.mapbox.android)

    // 导航路由
    implementation(libs.raamcosta.compose.destinations.core)
    ksp(libs.raamcosta.compose.destinations.ksp)

    // jetpack compose 框架
    implementation(libs.dl.compose)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.constraintlayout.compose)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    // APP工具
    implementation(libs.dl.app)
    // 通用计算
    implementation(libs.dl.common)
    // 天地图API
    implementation(libs.dl.tianditu)
    // ktor客户端
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    // jetpack datastore 封装工具
    implementation(libs.dl.datastore)
    ksp(libs.dl.datastore.processor)
}

afterEvaluate {
    publishing {
        publications {
            create("release", MavenPublication::class) {
                artifactId = "DLMapboxUtil"
                from(components.getByName("release"))
            }
        }
        repositories {
            maven {
                url = uri("/Users/d10ng/project/kotlin/maven-repo/repository")
            }
            maven {
                credentials {
                    username = bds100MavenUsername
                    password = bds100MavenPassword
                }
                setUrl("https://nexus.bds100.com/repository/maven-releases/")
            }
        }
    }
}