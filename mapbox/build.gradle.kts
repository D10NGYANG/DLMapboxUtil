val bds100MavenUsername: String by project
val bds100MavenPassword: String by project

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("com.google.devtools.ksp")
    id("maven-publish")
}

group = "com.github.D10NGYANG"
version = "1.1.0"

android {
    namespace = "com.d10ng.mapbox"
    compileSdk = Project.compile_sdk

    defaultConfig {
        minSdk = Project.min_sdk

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
    composeOptions {
        kotlinCompilerExtensionVersion = compose_compiler_ver
    }
    buildFeatures {
        compose = true
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

dependencies {
    // Android
    implementation("androidx.core:core-ktx:1.12.0")

    // 单元测试（可选）
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$jetpack_lifecycle_ver")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$jetpack_lifecycle_ver")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlin_coroutines_ver")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$kotlin_coroutines_ver")

    // kotlinx.serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlin_serialization_json")

    // startup
    implementation("androidx.startup:startup-runtime:1.1.1")

    // MapBox 地图
    api("com.mapbox.maps:android:10.16.2")

    // 导航路由
    implementation("io.github.raamcosta.compose-destinations:animations-core:$compose_destinations_ver")
    ksp("io.github.raamcosta.compose-destinations:ksp:$compose_destinations_ver")

    // jetpack compose 框架
    implementation("com.github.D10NGYANG:DLJetpackComposeUtil:2.0.5")
    // APP通用工具
    implementation("com.github.D10NGYANG:DLAppUtil:2.4.6")
    // 通用处理工具
    implementation("com.github.D10NGYANG:DLCommonUtil:0.0.3")
    // 天地图API
    implementation("com.github.D10NGYANG:DLTianDiTuApi:0.5.4")
    // 网络请求框架
    implementation("com.github.D10NGYANG:DLHttpUtil:0.8.5")
    // jetpack datastore 封装工具
    implementation("com.github.D10NGYANG:DLDatastoreUtil:0.0.2")
    ksp("com.github.D10NGYANG:DLDatastoreUtil-Processor:0.0.2")
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