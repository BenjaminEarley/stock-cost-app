import com.google.protobuf.gradle.generateProtoTasks
import com.google.protobuf.gradle.protobuf
import com.google.protobuf.gradle.protoc

plugins {
    android("application")
    kotlin("android")
    kotlin("kapt")
    androidx("navigation.safeargs.kotlin")
    id("kotlin-parcelize")
    id("com.google.protobuf") version Versions.Plugins.protobuf
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
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
        freeCompilerArgs += listOf(
            "-Xopt-in=kotlin.ExperimentalStdlibApi",
            "-Xopt-in=kotlin.time.ExperimentalTime",
            "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
            "-Xopt-in=kotlinx.coroutines.FlowPreview"
        )
    }
}

dependencies {
    implementation(Dependencies.Kotlin.Stdlib)
    implementation(Dependencies.Kotlin.Coroutines.Core)
    implementation(Dependencies.Kotlin.Coroutines.Android)
    implementation(Dependencies.Androidx.Activity)
    implementation(Dependencies.Androidx.Annotation)
    implementation(Dependencies.Androidx.AppCompat)
    implementation(Dependencies.Androidx.Core)
    implementation(Dependencies.Androidx.CardView)
    implementation(Dependencies.Androidx.DataStore)
    implementation(Dependencies.Androidx.Fragment)
    implementation(Dependencies.Androidx.RecyclerView)
    implementation(Dependencies.Androidx.ConstraintLayout)
    implementation(Dependencies.Androidx.CoordinatorLayout)
    implementation(Dependencies.Androidx.Lifecycle)
    implementation(Dependencies.Androidx.LifecycleViewModel)
    implementation(Dependencies.Androidx.LifecycleViewModelSavedState)
    implementation(Dependencies.Androidx.LifecycleLiveData)
    kapt(Dependencies.Androidx.LifecycleCompiler)
    implementation(Dependencies.Androidx.SavedState)
    implementation(Dependencies.Androidx.Navigation)
    implementation(Dependencies.Androidx.NavigationFragment)
    implementation(Dependencies.Androidx.HiltNavigation)
    kapt(Dependencies.Androidx.HiltCompiler)
    implementation(Dependencies.Google.Material)
    implementation(Dependencies.Google.DI.Hilt)
    kapt(Dependencies.Google.DI.HiltCompiler)
    implementation(Dependencies.Google.Serializer.Protobuf)
    implementation(Dependencies.Networking.Moshi)
    implementation(Dependencies.Networking.MoshiAdapters)
    kapt(Dependencies.Networking.MoshiCodegen)
    implementation(platform(Dependencies.Networking.OkhttpBom))
    implementation(Dependencies.Networking.Okhttp)
    implementation(Dependencies.Networking.Retrofit)
    implementation(Dependencies.Networking.RetrofitConverterMoshi)
    implementation(Dependencies.Logging.Timber)
    debugImplementation(Dependencies.Networking.OkhttpLogger)
    testImplementation(Dependencies.Test.JUnit)
    testImplementation(Dependencies.Test.Truth)
}

protobuf {
    protoc {
        artifact = Dependencies.Google.Serializer.Protoc
    }
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                create("java") {
                    option("lite")
                }
            }
        }
    }
}