import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)

    // kotlin version
    kotlin("plugin.serialization") version "2.1.0"

    // BuildConfig
    id("com.github.gmazzo.buildconfig") version "6.0.4"

    // DB
    id("app.cash.sqldelight") version "2.1.0"
}

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(localPropertiesFile.inputStream())
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
            linkerOpts("-lsqlite3")
        }
    }
    
    jvm()
    
    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)

            // Client Requests for Android
            implementation("io.ktor:ktor-client-okhttp:3.2.2")

            // Context
            implementation("io.insert-koin:koin-android:4.1.+")

            // DB
            implementation("app.cash.sqldelight:android-driver:2.1.0")

            // Carousel
            implementation("androidx.compose.foundation:foundation:1.6.0")
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation("androidx.compose.ui:ui-text-google-fonts:1.6.0")

            // Interface
            implementation("io.ktor:ktor-client-core:3.2.2")

            // Serialization
            implementation("io.ktor:ktor-client-content-negotiation:3.2.2")
            implementation("io.ktor:ktor-serialization-kotlinx-json:3.2.2")

            // Logger
            implementation("io.ktor:ktor-client-logging:3.2.2")

            // Icons
            implementation("org.jetbrains.compose.material:material-icons-extended:1.7.3")

            // ViewModel
            implementation("org.jetbrains.androidx.lifecycle:lifecycle-viewmodel-compose:2.9.+")

            // Image Loading
            implementation("io.coil-kt.coil3:coil-network-ktor3:3.2.0")
            implementation("io.coil-kt.coil3:coil-compose:3.2.0")

            // Navigation
            implementation("org.jetbrains.androidx.navigation:navigation-compose:2.9.+")

            // Koin
            implementation("io.insert-koin:koin-compose:4.1.+")
            implementation("io.insert-koin:koin-compose-viewmodel:4.1.+")
            implementation("io.insert-koin:koin-compose-viewmodel-navigation:4.1.+")

            // DB
            implementation("app.cash.sqldelight:runtime:2.1.0")
            implementation("app.cash.sqldelight:coroutines-extensions:2.1.0")

            // Date Time
            implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.7.1")
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)

            // Client Requests for Desktop JVM
            implementation("io.ktor:ktor-client-okhttp:3.2.2")

            // DB
            implementation("app.cash.sqldelight:sqlite-driver:2.1.0")
        }
        iosMain.dependencies{
            // Client Requests for iOS
            implementation("io.ktor:ktor-client-darwin:3.2.2")

            // DB
            implementation("app.cash.sqldelight:native-driver:2.1.0")
        }
    }
}

buildConfig {
    className("BuildConfig")
    packageName("org.example.project")

    val apiKey = localProperties.getProperty("tmdb.api.key") ?: ""
    buildConfigField("String", "API_KEY", "\"$apiKey\"")
}

android {
    namespace = "org.example.project"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "org.example.project"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "org.example.project.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "org.example.project"
            packageVersion = "1.0.0"

            macOS {
                iconFile.set(project.file("src/jvmMain/resources/Icon.png"))
            }
            windows {
                iconFile.set(project.file("src/jvmMain/resources/Icon.ico"))
            }
            linux {
                iconFile.set(project.file("src/jvmMain/resources/Icon.png"))
            }
        }
    }
}

sqldelight {
    databases {
        create("SeenItDatabase") {
            packageName.set("org.example.project.db")
        }
    }
}