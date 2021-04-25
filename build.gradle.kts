buildscript {
    repositories {
        google()
        mavenCentral()

    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.1.3")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.Plugins.kotlin}")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.Plugins.xNavigation}")
        classpath("com.google.dagger:hilt-android-gradle-plugin:${Versions.Plugins.hilt}")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

tasks {
    val clean by registering(Delete::class) {
        delete(buildDir)
    }
}