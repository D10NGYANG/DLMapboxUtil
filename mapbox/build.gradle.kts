val bds100MavenUsername: String by project
val bds100MavenPassword: String by project

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
    id("org.jetbrains.kotlin.plugin.parcelize")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("com.google.devtools.ksp")
    id("maven-publish")
}

group = "com.github.D10NGYANG"
version = "0.9.6"

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
    implementation("androidx.core:core-ktx:1.10.0")

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

    // MapBox 地图
    api("com.mapbox.maps:android:10.11.2")

    // 导航路由
    implementation("io.github.raamcosta.compose-destinations:animations-core:$compose_destinations_ver")
    ksp("io.github.raamcosta.compose-destinations:ksp:$compose_destinations_ver")

    // jetpack compose 框架
    implementation("com.github.D10NGYANG:DLJetpackComposeUtil:1.3.2")
    // APP通用工具
    implementation("com.github.D10NGYANG:DLAppUtil:2.3.0")
    // 定位工具
    implementation("com.github.D10NGYANG:DLGpsUtil:2.2.1")
    // 经纬度工具
    implementation("com.github.D10NGYANG:DLLatLngUtil-jvm:1.6.8")
    // 天地图API
    implementation("com.github.D10NGYANG:DLTianDiTuApi-jvm:0.5.0")
    // 网络请求框架
    implementation("com.github.D10NGYANG:DLHttpUtil-jvm:0.8.0")
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