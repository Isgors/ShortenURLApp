import com.android.build.api.dsl.ManagedVirtualDevice

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.room)
    alias(libs.plugins.ksp)
}


android {
    namespace = "dev.igordesouza.shortenurlapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "dev.igordesouza.shortenurlapp"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        testInstrumentationRunnerArguments += mapOf(
            // Default behavior: run only release-gate tests
            "category" to "dev.igordesouza.shortenurlapp.tags.ReleaseGate",
            // Always exclude flaky tests unless explicitly requested
            "notCategory" to "dev.igordesouza.shortenurlapp.tags.Flaky",
            "clearPackageData" to "true"
        )

        buildConfigField("String", "BASE_URL", "\"https://url-shortener-server.onrender.com/\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }

    room {
        schemaDirectory("$projectDir/schemas")
    }

    testOptions {
        execution = "ANDROIDX_TEST_ORCHESTRATOR"
        managedDevices {
            allDevices {
                // Fast feedback device (release gate)
                maybeCreate<ManagedVirtualDevice>("pixel6Api34").apply {
                    device = "Pixel 6"
                    apiLevel = 34
                    systemImageSource = "aosp"
                }

                // Lower API (compat regression)
                maybeCreate<ManagedVirtualDevice>("pixel4Api30").apply {
                    device = "Pixel 4"
                    apiLevel = 30
                    systemImageSource = "aosp"
                }
            }

            groups {
                maybeCreate("releaseGateDevices").apply {
                    targetDevices.add(allDevices["pixel6Api34"])
                }

                maybeCreate("fullMatrixDevices").apply {
                    targetDevices.add(allDevices["pixel6Api34"])
                    targetDevices.add(allDevices["pixel4Api30"])
                }
            }
        }
    }
}

dependencies {
    androidTestUtil(libs.androidx.orchestrator)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.compose.materialIcons)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.koin.compose)
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    implementation(libs.androidx.compose.ui.test)
    ksp(libs.room.compiler)
    testImplementation(libs.junit)
    testImplementation(libs.turbine)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk)
    androidTestImplementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
    androidTestImplementation(libs.androidx.uiautomator)
    androidTestImplementation(libs.androidx.rules)
    androidTestImplementation(libs.mockk.android)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}