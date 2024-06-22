import com.android.build.api.dsl.AndroidSourceSet
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import com.google.protobuf.gradle.id
import org.jetbrains.kotlin.gradle.plugin.extraProperties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.10"
    id("com.google.gms.google-services")
    id("com.google.protobuf") version "0.9.1"
    id("com.chaquo.python")
    id("com.google.firebase.crashlytics")
}

chaquopy {
    defaultConfig {
        buildPython("/usr/local/bin/python3")
        buildPython("python3")
        version = "3.8"

        pip{
            options("--upgrade","--ignore-installed","--force-reinstall")
            install("yt-dlp")
            install("git+https://github.com/oncename/pytube")
            install("ffmpeg")
        }
    }
    sourceSets {
        getByName("main") {
            srcDirs("src/main/python")
        }
    }
}

android {
    namespace = "com.application.moviesapp"
    compileSdk = 33

    signingConfigs {
        create("config") {
            keyAlias = "moviesapp"
            keyPassword = "Sheikh"
            storeFile = file("/media/sheikh/hdd/AndroidStudioProjects/AndroidStudioProjects/MoviesApp/app/keystore.jks")
            storePassword = "Sheikh"
            enableV4Signing = true
        }
    }

    defaultConfig {
        applicationId = "com.application.moviesapp"
        minSdk = 24
        targetSdk = 33
        versionCode = 20
        versionName = "1.0.19"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        compileSdkPreview = "UpsideDownCake"

        ndk {
            abiFilters += listOf("armeabi-v7a", "arm64-v8a", "x86_64")
        }
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    buildTypes {
        getByName("debug") {
            isDebuggable = true
            isMinifyEnabled = false

            val TEST_API_KEY: String by project
            val FACEBOOK_APP_ID: String by project
            val YOUTUBE_API_KEY: String by project

            buildConfigField(type = "String", name =  "API_KEY", value = TEST_API_KEY)
            buildConfigField(type = "String", name = "BASE_URL", value = "\"https://api.themoviedb.org/\"")
            buildConfigField(type = "String", name = "IMAGE_BASE_URL", value = "\"https://image.tmdb.org/t/p/original/\"")
            buildConfigField(type = "String", name = "YOUTUBE_API_KEY", value = YOUTUBE_API_KEY)
            buildConfigField(type = "String", name = "YOUTUBE_BASE_URL", value = "\"https://youtube.googleapis.com/\"")
            resValue("string", "FACEBOOK_APP_ID", FACEBOOK_APP_ID)

        }
        getByName("release") {
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            val PRO_API_KEY: String by project
            val FACEBOOK_APP_ID: String by project
            val YOUTUBE_API_KEY: String by project

            buildConfigField(type = "String", name = "API_KEY", value = PRO_API_KEY)
            buildConfigField(type = "String", name = "BASE_URL", value = "\"https://api.themoviedb.org/\"")
            buildConfigField(type = "String", name = "IMAGE_BASE_URL", value = "\"https://image.tmdb.org/t/p/original/\"")
            buildConfigField(type = "String", name = "YOUTUBE_API_KEY", value = YOUTUBE_API_KEY)
            buildConfigField(type = "String", name = "YOUTUBE_BASE_URL", value = "\"https://youtube.googleapis.com/\"")
            resValue("string", "FACEBOOK_APP_ID", FACEBOOK_APP_ID)

            signingConfig = signingConfigs.getByName("config")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

//    flavorDimensions += "pyVersion"
//    productFlavors {
//        create("py310") { dimension = "pyVersion" }
//        create("py311") { dimension = "pyVersion" }
//    }
}

dependencies {

    // core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.compose)

    // material3
    implementation(libs.compose.material3)

    // test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.compose.junit)
    debugImplementation(libs.compose.ui.tooling)
    debugImplementation(libs.compose.test.manifest)

    // firebase
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.database)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.messaging)

    // splash
    implementation(libs.androidx.core.splashscreen)

    // Dagger - Hilt
    implementation(libs.dagger.hilt)
    kapt(libs.dagger.hilt.compiler)
    implementation(libs.dagger.nav.compose)

    // ViewModel with ktx
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    // Compose viewmodel utility
    implementation(libs.androidx.lifecycle.viewmodel.compose)


    // Timber for log
    implementation(libs.timber)

    // Retrofit
    implementation(libs.retrofit2)
    implementation(libs.okhttp)

    // Retrofit with Scalar Converter
    implementation(libs.scalar.converter)

    // Kotlin Serialization
    implementation(libs.kotlin.serialization)

    // Retrofit with Jakewharton Converter
    implementation(libs.jakewharton.converter)

    // Lottie
    implementation(libs.lottie)

    implementation(libs.accompanist.systemuicontroller)

    // Material icon extended
    implementation(libs.androidx.material.icons.extended)

    // Coil
    implementation(libs.coil.compose)

    // Paging 3
    implementation(libs.androidx.paging.runtime.ktx)
    implementation(libs.androidx.paging.compose)

    // Room
    implementation(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.paging)

    implementation(libs.firebase.auth.ktx)
    implementation(libs.play.services.auth)

    implementation(libs.facebook.android.sdk )
    implementation(libs.facebook.login)

    // Pager
    implementation(libs.accompanist.pager)
    implementation(libs.accompanist.pager.indicators)

    // Media 3
    implementation(libs.androidx.media3.ui)
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.session)

    // Flow layout
    implementation(libs.accompanist.flowlayout)

    // Proto datastore
    implementation(libs.androidx.datastore)
    implementation(libs.protobuf.javalite)
    implementation(libs.protobuf.kotlin.lite)

    // Preferences datastore
    implementation(libs.androidx.datastore.preferences)

    //Work Manager
    implementation(libs.androidx.work.runtime.ktx)

    //Work Manager + Hilt
    implementation(libs.androidx.hilt.work)
    kapt(libs.dagger.hilt.compiler)
    kapt(libs.androidx.hilt.compiler)

    //app start up
    implementation(libs.androidx.startup.runtime)

    //Permission
    implementation(libs.accompanist.permissions)

    //Constaint Layout
    implementation(libs.androidx.constraintlayout.compose)

    //Youtube Player
    implementation(libs.youtube.player.core)

    //Youtube Url Extracter
    implementation(libs.kotlin.youtubeExtractor)
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.21.9"
    }

    // Generates the java Protobuf-lite code for the Protobufs in this project. See
    // https://github.com/google/protobuf-gradle-plugin#customizing-protobuf-compilation
    // for more information.
    generateProtoTasks {
        // see https://github.com/google/protobuf-gradle-plugin/issues/518
        // see https://github.com/google/protobuf-gradle-plugin/issues/491

        // all() here because of android multi-variant
        all().forEach { task ->
            // this only works on version 3.8+ that has buildins for javalite / kotlin lite
            // with previous version the java build in is to be removed and a new plugin
            // need to be declared
            task.builtins {
                id("java") { // id is imported above
                    option("lite")
                }
                id("kotlin") {
                    option("lite")
                }
            }
        }
    }
}