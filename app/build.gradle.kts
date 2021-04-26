plugins {
    android("application")
    kotlin("android")
    kotlin("kapt")
    kotlin("plugin.serialization") version Versions.Plugins.kotlin
    kotlin("plugin.parcelize")
    androidx("navigation.safeargs.kotlin")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdkVersion(Versions.Platform.sdk)
    buildToolsVersion(Versions.Platform.buildTool)

    defaultConfig {
        applicationId = "com.benjaminearley.stockcost"
        minSdkVersion(Versions.Platform.minSdk)
        targetSdkVersion(Versions.Platform.sdk)
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        javaCompileOptions {
            annotationProcessorOptions {
                arguments.plusAssign(arrayOf("room.schemaLocation" to "$projectDir/schemas"))
            }
        }

        buildConfigField(
            "String",
            "BASE_API_URL",
            "\"https://api.beta.getbux.com\""
        )

        buildConfigField(
            "String",
            "BASE_SOCKET_URL",
            "\"https://rtf.beta.getbux.com\""
        )
    }
    buildFeatures {
        viewBinding = true
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs["debug"]
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
        freeCompilerArgs = listOf(
            "-Xopt-in=kotlin.ExperimentalStdlibApi",
            "-Xopt-in=kotlin.time.ExperimentalTime",
            "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
            "-Xopt-in=kotlinx.coroutines.FlowPreview"
        )
    }
}

dependencies {
    implementation(Dependencies.Kotlin.Stdlib)
    implementation(Dependencies.Kotlin.Serialization)
    implementation(Dependencies.Kotlin.Coroutines.Core)
    implementation(Dependencies.Kotlin.Coroutines.Android)
    implementation(Dependencies.Androidx.Activity)
    implementation(Dependencies.Androidx.Annotation)
    implementation(Dependencies.Androidx.AppCompat)
    implementation(Dependencies.Androidx.Core)
    implementation(Dependencies.Androidx.CardView)
    implementation(Dependencies.Androidx.Fragment)
    implementation(Dependencies.Androidx.RecyclerView)
    implementation(Dependencies.Androidx.ConstraintLayout)
    implementation(Dependencies.Androidx.CoordinatorLayout)
    implementation(Dependencies.Androidx.Lifecycle)
    implementation(Dependencies.Androidx.LifecycleViewModel)
    implementation(Dependencies.Androidx.LifecycleViewModelSavedState)
    implementation(Dependencies.Androidx.LifecycleLiveData)
    implementation(Dependencies.Androidx.LifecycleCommon)
    implementation(Dependencies.Androidx.SavedState)
    implementation(Dependencies.Androidx.Navigation)
    implementation(Dependencies.Androidx.NavigationFragment)
    implementation(Dependencies.Androidx.HiltNavigation)
    kapt(Dependencies.Androidx.HiltCompiler)
    implementation(Dependencies.Androidx.RoomRuntime)
    implementation(Dependencies.Androidx.Room)
    kapt(Dependencies.Androidx.RoomCompiler)
    implementation(Dependencies.Google.Material)
    implementation(Dependencies.Google.DI.Hilt)
    kapt(Dependencies.Google.DI.HiltCompiler)
    implementation(platform(Dependencies.Networking.OkhttpBom))
    implementation(Dependencies.Networking.Okhttp)
    implementation(Dependencies.Networking.Ktor)
    implementation(Dependencies.Networking.KtorOkhttp)
    implementation(Dependencies.Networking.KtorWebsockets)
    implementation(Dependencies.Networking.KtorSerialization)
    implementation(Dependencies.Networking.KtorLogging)
    implementation(Dependencies.Logging.Timber)
    implementation(Dependencies.Arrow.Bom)
    implementation(Dependencies.Arrow.Core)
    implementation(Dependencies.Arrow.Fx)
    debugImplementation(Dependencies.Networking.OkhttpLogger)
    testImplementation(Dependencies.Test.JUnit)
    testImplementation(Dependencies.Test.Truth)
}