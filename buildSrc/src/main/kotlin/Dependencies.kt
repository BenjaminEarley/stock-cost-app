// @formatter:off
@file:Suppress("SpellCheckingInspection")

object Versions {
    object Library {
        // Language
        const val kotlin = "1.4.32" //https://kotlinlang.org/releases.html#release-details
        const val coroutines = "1.4.3" //https://github.com/Kotlin/kotlinx.coroutines/releases

        // AndroidX https://developer.android.com/jetpack/androidx/versions#version-table
        const val xActivity = "1.2.2" //https://developer.android.com/jetpack/androidx/releases/activity
        const val xAnnotation = "1.2.0" //https://developer.android.com/jetpack/androidx/releases/annotation
        const val xAppCompat = "1.2.0" //https://developer.android.com/jetpack/androidx/releases/appcompat
        const val xCore = "1.3.2" //https://developer.android.com/jetpack/androidx/releases/core
        const val xCardView = "1.0.0" //https://developer.android.com/jetpack/androidx/releases/cardview
        const val xDataStore = "1.0.0-alpha08" //https://developer.android.com/jetpack/androidx/releases/datastore
        const val xStartup = "1.0.0" //https://developer.android.com/jetpack/androidx/releases/startup
        const val xAutofill = "1.1.0" //https://developer.android.com/jetpack/androidx/releases/autofill
        const val xFragment = "1.3.3" //https://developer.android.com/jetpack/androidx/releases/fragment
        const val xConstraintLayout = "2.0.4" //https://developer.android.com/jetpack/androidx/releases/constraintlayout
        const val xCoordinatorLayout = "1.1.0" //https://developer.android.com/jetpack/androidx/releases/coordinatorlayout
        const val xSwipeRefreshLayout = "1.1.0" //https://developer.android.com/jetpack/androidx/releases/swiperefreshlayout
        const val xDrawerLayout = "1.1.1" //https://developer.android.com/jetpack/androidx/releases/drawerlayout
        const val xLifecycle = "2.3.1" //https://developer.android.com/jetpack/androidx/releases/lifecycle
        const val xNavigation = "2.3.5" //https://developer.android.com/jetpack/androidx/releases/navigation
        const val xRecyclerview = "1.2.0" //https://developer.android.com/jetpack/androidx/releases/recyclerview
        const val xSecurity = "1.0.0" //https://developer.android.com/jetpack/androidx/releases/security
        const val xBrowser = "1.3.0" //https://developer.android.com/jetpack/androidx/releases/browser
        const val xWork = "2.5.0" //https://developer.android.com/jetpack/androidx/releases/work
        const val xRoom = "2.3.0" //https://developer.android.com/jetpack/androidx/releases/room
        const val xSavedState = "1.1.0" //https://developer.android.com/jetpack/androidx/releases/savedstate
        const val xHilt = "1.0.0-beta01" //https://developer.android.com/jetpack/androidx/releases/hilt
        const val xEmoji = "1.1.0" //https://developer.android.com/jetpack/androidx/releases/emoji
        const val xTransition = "1.4.1" //https://developer.android.com/jetpack/androidx/releases/transition

        // Firebase
        const val firebaseBom = "27.1.0" //https://firebase.google.com/support/release-notes/android

        // Material
        const val material = "1.3.0" //https://github.com/material-components/material-components-android/releases

        // Networking
        const val stetho = "1.6.0" //https://github.com/facebookarchive/stetho/releases
        const val moshi = "1.12.0" //https://github.com/square/moshi/releases
        const val okHttp = "4.9.1" //https://github.com/square/okhttp/releases
        const val retrofit = "2.9.0" //https://github.com/square/retrofit/releases
        const val glide = "4.12.0" //https://github.com/bumptech/glide/releases
        const val ktor = "1.5.3" //https://github.com/ktorio/ktor/releases

        // Phone Number Validation
        const val libPhoneNumber = "8.12.21" //https://github.com/MichaelRocks/libphonenumber-android/releases

        // recyclerview abstraction
        const val epoxy = "4.5.0" //https://github.com/airbnb/epoxy/releases

        // dependency injection
        const val hilt = "2.34.1-beta" //https://github.com/google/dagger/releases

        // play services https://developers.google.com/android/guides/setup#list-dependencies
        const val location = "18.0.0"
        const val auth = "19.0.0"

        // protobuf
        const val protobufLite = "3.15.8" //https://mvnrepository.com/artifact/com.google.protobuf/protobuf-javalite
        const val protoc = "3.15.8" //https://mvnrepository.com/artifact/com.google.protobuf/protoc

        // Facebook https://github.com/facebook/facebook-android-sdk/releases
        const val facebook = "9.1.1"

        // Arrow https://github.com/arrow-kt/arrow/releases
        const val arrow = "0.13.1"

        // Logging
        const val timber = "4.7.1" //https://github.com/JakeWharton/timber/releases
        const val datadog = "1.8.1" //https://github.com/DataDog/dd-sdk-android/releases



        // testing
        const val junit = "4.13.2" //https://github.com/junit-team/junit4/releases
        const val truth = "1.1.2" //https://github.com/google/truth/releases
    }

    object Platform {
        const val sdk = 30
        const val minSdk = 23
        const val buildTool = "29.0.3" //https://developer.android.com/studio/releases/build-tools#notes
    }

    object Plugins {
        const val ktlint = "10.0.0" //https://github.com/JLLeitschuh/ktlint-gradle/releases
        const val dokka = "0.10.1" //https://github.com/Kotlin/dokka/releases
        const val googleServices = "4.3.5" //https://developers.google.com/android/guides/google-services-plugin#introduction
        const val firebaseAppDistribution = "2.1.0" //https://firebase.google.com/docs/app-distribution/android/distribute-gradle#step_1_set_up_your_android_project
        const val firebaseCrashlytics = "2.5.2" //https://firebase.google.com/docs/crashlytics/get-started?platform=android#add-sdk
        const val protobuf = "0.8.15" //https://mvnrepository.com/artifact/com.google.protobuf/protobuf-gradle-plugin
        const val xNavigation = Versions.Library.xNavigation
        const val kotlin = Versions.Library.kotlin
        const val hilt = Versions.Library.hilt
    }
}

object Dependencies {

    object Kotlin {
        const val Stdlib = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.Library.kotlin}"
        const val Serialization = "org.jetbrains.kotlin:kotlin-serialization:${Versions.Library.kotlin}"

        object Coroutines {
            const val Core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.Library.coroutines}"
            const val Android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.Library.coroutines}"
            const val Play = "org.jetbrains.kotlinx:kotlinx-coroutines-play-services:${Versions.Library.coroutines}"
        }
    }

    object Google  {
        const val Material = "com.google.android.material:material:${Versions.Library.material}"
        object Serializer {
            const val Protobuf = "com.google.protobuf:protobuf-javalite:${Versions.Library.protobufLite}"
            const val Protoc = "com.google.protobuf:protoc:${Versions.Library.protoc}"
        }

        object Firebase {
            const val Bom = "com.google.firebase:firebase-bom:${Versions.Library.firebaseBom}"
            const val Analytics = "com.google.firebase:firebase-analytics-ktx"
            const val Crashlytics = "com.google.firebase:firebase-crashlytics-ktx"
            const val Functions = "com.google.firebase:firebase-functions-ktx"
            const val DynamicLinks = "com.google.firebase:firebase-dynamic-links-ktx"
        }
        object Play {
            const val Location = "com.google.android.gms:play-services-location:${Versions.Library.location}"
            const val Auth = "com.google.android.gms:play-services-auth:${Versions.Library.auth}"
        }
        object DI {
            const val Hilt = "com.google.dagger:hilt-android:${Versions.Library.hilt}"
            const val HiltCompiler = "com.google.dagger:hilt-compiler:${Versions.Library.hilt}"
        }
    }

    object Androidx {
        const val Activity = "androidx.activity:activity-ktx:${Versions.Library.xActivity}"
        const val Annotation = "androidx.annotation:annotation:${Versions.Library.xAnnotation}"
        const val AppCompat = "androidx.appcompat:appcompat:${Versions.Library.xAppCompat}"
        const val Autofill = "androidx.autofill:autofill:${Versions.Library.xAutofill}"
        const val Core = "androidx.core:core-ktx:${Versions.Library.xCore}"
        const val CardView = "androidx.cardview:cardview:${Versions.Library.xCardView}"
        const val DataStore = "androidx.datastore:datastore:${Versions.Library.xDataStore}"
        const val DataStorePreferences = "androidx.datastore:datastore-preferences:${Versions.Library.xDataStore}"
        const val Fragment = "androidx.fragment:fragment-ktx:${Versions.Library.xFragment}"
        const val RecyclerView = "androidx.recyclerview:recyclerview:${Versions.Library.xRecyclerview}"
        const val ConstraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.Library.xConstraintLayout}"
        const val CoordinatorLayout = "androidx.coordinatorlayout:coordinatorlayout:${Versions.Library.xCoordinatorLayout}"
        const val SwipeRefreshLayout = "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.Library.xSwipeRefreshLayout}"
        const val DrawerLayout = "androidx.drawerlayout:drawerlayout:${Versions.Library.xDrawerLayout}"
        const val Lifecycle = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.Library.xLifecycle}"
        const val LifecycleLiveData = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.Library.xLifecycle}"
        const val LifecycleViewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.Library.xLifecycle}"
        const val LifecycleViewModelSavedState = "androidx.lifecycle:lifecycle-viewmodel-savedstate:${Versions.Library.xLifecycle}"
        const val LifecycleCompiler = "androidx.lifecycle:lifecycle-compiler:${Versions.Library.xLifecycle}"
        const val LifecycleCommon = "androidx.lifecycle:lifecycle-common-java8:${Versions.Library.xLifecycle}"
        const val SavedState = "androidx.savedstate:savedstate:${Versions.Library.xSavedState}"
        const val Navigation = "androidx.navigation:navigation-ui-ktx:${Versions.Library.xNavigation}"
        const val NavigationFragment = "androidx.navigation:navigation-fragment-ktx:${Versions.Library.xNavigation}"
        const val Security = "androidx.security:security-crypto:${Versions.Library.xSecurity}"
        const val Browser = "androidx.browser:browser:${Versions.Library.xBrowser}"
        const val Work = "androidx.work:work-runtime-ktx:${Versions.Library.xWork}"
        const val RoomRuntime = "androidx.room:room-runtime:${Versions.Library.xRoom}"
        const val Room = "androidx.room:room-ktx:${Versions.Library.xRoom}"
        const val RoomCompiler = "androidx.room:room-compiler:${Versions.Library.xRoom}"
        const val Startup = "androidx.startup:startup-runtime:${Versions.Library.xStartup}"
        const val HiltNavigation = "androidx.hilt:hilt-navigation-fragment:${Versions.Library.xHilt}"
        const val HiltWork = "androidx.hilt:hilt-work:${Versions.Library.xHilt}"
        const val HiltCompiler = "androidx.hilt:hilt-compiler:${Versions.Library.xHilt}"
        const val Emoji = "androidx.emoji:emoji:${Versions.Library.xEmoji}"
        const val EmojiAppCompat = "androidx.emoji:emoji-appcompat:${Versions.Library.xEmoji}"
        const val Transition = "androidx.transition:transition-ktx:${Versions.Library.xTransition}"
    }

    object Util {
        const val LibPhoneNumber = "io.michaelrocks:libphonenumber-android:${Versions.Library.libPhoneNumber}"
    }

    object Networking {
        const val Moshi = "com.squareup.moshi:moshi:${Versions.Library.moshi}"
        const val MoshiAdapters = "com.squareup.moshi:moshi-adapters:${Versions.Library.moshi}"
        const val MoshiCodegen = "com.squareup.moshi:moshi-kotlin-codegen:${Versions.Library.moshi}"
        const val OkhttpBom = "com.squareup.okhttp3:okhttp-bom:${Versions.Library.okHttp}"
        const val Okhttp = "com.squareup.okhttp3:okhttp"
        const val OkhttpLogger = "com.squareup.okhttp3:logging-interceptor"
        const val Retrofit = "com.squareup.retrofit2:retrofit:${Versions.Library.retrofit}"
        const val RetrofitConverterMoshi = "com.squareup.retrofit2:converter-moshi:${Versions.Library.retrofit}"
        const val Glide = "com.github.bumptech.glide:glide:${Versions.Library.glide}"
        const val GlideIntegrationOkhttp = "com.github.bumptech.glide:okhttp3-integration:${Versions.Library.glide}"
        const val GlideCompiler = "com.github.bumptech.glide:compiler:${Versions.Library.glide}"
        const val Stetho = "com.facebook.stetho:stetho:${Versions.Library.stetho}"
        const val OkhttpInterceptorStetho = "com.facebook.stetho:stetho-okhttp3:${Versions.Library.stetho}"
        const val Ktor = "io.ktor:ktor-client-core:${Versions.Library.ktor}"
        const val KtorOkhttp = "io.ktor:ktor-client-okhttp:${Versions.Library.ktor}"
        const val KtorWebsockets = "io.ktor:ktor-client-websockets:${Versions.Library.ktor}"
        const val KtorSerialization = "io.ktor:ktor-client-serialization:${Versions.Library.ktor}"
        const val KtorLogging = "io.ktor:ktor-client-logging:${Versions.Library.ktor}"
    }

    object Epoxy {
        const val Lib = "com.airbnb.android:epoxy:${Versions.Library.epoxy}"
        const val Processor = "com.airbnb.android:epoxy-processor:${Versions.Library.epoxy}"
        const val GlidePreloading = "com.airbnb.android:epoxy-glide-preloading:${Versions.Library.epoxy}"
    }

    object Facebook {
        const val Login = "com.facebook.android:facebook-login:${Versions.Library.facebook}"
    }

    object Arrow {
        const val Bom = "io.arrow-kt:arrow-stack:${Versions.Library.arrow}"
        const val Core = "io.arrow-kt:arrow-core"
        const val Fx = "io.arrow-kt:arrow-fx-coroutines"
    }

    object Logging {
        const val Timber = "com.jakewharton.timber:timber:${Versions.Library.timber}"
    }

    object Test {
        const val JUnit = "junit:junit:${Versions.Library.junit}"
        const val Truth = "com.google.truth:truth:${Versions.Library.truth}"
    }
}