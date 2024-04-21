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
        versionCode = 3
        versionName = "1.0.2"

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

    val lifecycle_version = "2.6.1"
    val timber_version = "5.0.1"
    val lottie_version = "6.1.0"
    val paging_version = "3.2.1"
    val room_version = "2.5.2"
    val firebase_version = "22.1.1"

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
    implementation("com.airbnb.android:lottie:$lottie_version")

    implementation("com.google.accompanist:accompanist-systemuicontroller:0.31.6-rc")

    // Material icon extended
    implementation("androidx.compose.material:material-icons-extended:1.6.0-alpha02")

    // Coil
    implementation("io.coil-kt:coil-compose:2.4.0")

    // Paging 3
    implementation("androidx.paging:paging-runtime-ktx:$paging_version")
    implementation("androidx.paging:paging-compose:$paging_version")

    // Room
    implementation("androidx.room:room-runtime:$room_version")
    kapt("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    implementation("androidx.room:room-paging:$room_version")

    implementation("com.google.firebase:firebase-auth-ktx:$firebase_version")
    implementation("com.google.android.gms:play-services-auth:20.6.0")

    implementation("com.facebook.android:facebook-android-sdk:12.1.0")
    implementation ("com.facebook.android:facebook-login:14.1.0")

    // Pager
    implementation("com.google.accompanist:accompanist-pager:0.23.1")
    implementation("com.google.accompanist:accompanist-pager-indicators:0.23.1")

    // Media 3
    implementation("androidx.media3:media3-ui:1.2.0-alpha01")
    implementation("androidx.media3:media3-exoplayer:1.2.0-alpha01")
    implementation("androidx.media3:media3-session:1.2.0-alpha01")

    // Flow layout
    implementation("com.google.accompanist:accompanist-flowlayout:0.32.0")

    // Proto datastore
    implementation("androidx.datastore:datastore:1.0.0")
    implementation("com.google.protobuf:protobuf-javalite:3.21.9")
    implementation("com.google.protobuf:protobuf-kotlin-lite:3.21.9")

    // Preferences datastore
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    //Work Manager
    implementation("androidx.work:work-runtime-ktx:2.8.1")

    //Work Manager + Hilt
    implementation("androidx.hilt:hilt-work:1.0.0")
    kapt("com.google.dagger:hilt-android-compiler:2.44")
    kapt("androidx.hilt:hilt-compiler:1.0.0")


    //app start up
    implementation("androidx.startup:startup-runtime:1.1.1")

    //Media3 Exoplayer
    implementation("androidx.media3:media3-exoplayer:1.2.0-rc01")
    implementation("androidx.media3:media3-ui:1.2.0-rc01")

    //Permission
    implementation("com.google.accompanist:accompanist-permissions:0.21.1-beta")

    //Constaint Layout
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")

    //Youtube Player
    implementation("com.pierfrancescosoffritti.androidyoutubeplayer:core:12.1.0")

    //Youtube Url Extracter
    implementation("com.github.maxrave-dev:kotlin-youtubeExtractor:0.0.7")
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