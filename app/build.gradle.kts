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

    defaultConfig {
        applicationId = "com.application.moviesapp"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

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
        debug {
            isDebuggable = true
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
        release {

            isDebuggable = false
            isMinifyEnabled = false
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

    flavorDimensions += "pyVersion"
    productFlavors {
        create("py310") { dimension = "pyVersion" }
        create("py311") { dimension = "pyVersion" }
    }
}

dependencies {
    implementation("com.google.firebase:firebase-crashlytics:18.6.0")
    implementation("com.google.firebase:firebase-analytics:21.5.0")
    implementation("com.google.firebase:firebase-database:20.3.0")
    implementation("com.google.firebase:firebase-storage:20.3.0")
    implementation("com.google.firebase:firebase-messaging:23.4.0")
    val lifecycle_version = "2.6.1"
    val timber_version = "5.0.1"
    val lottie_version = "6.1.0"
    val paging_version = "3.2.1"
    val room_version = "2.5.2"
    val firebase_version = "22.1.1"

    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")

    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3:1.2.0-alpha05")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    implementation("androidx.core:core-splashscreen:1.1.0-alpha01")

    // Dagger - Hilt
    implementation("com.google.dagger:hilt-android:2.44")
    kapt("com.google.dagger:hilt-android-compiler:2.44")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")

    // ViewModel with ktx
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")

    // Compose viewmodel utility
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycle_version")


    // Timber for log
    implementation("com.jakewharton.timber:timber:$timber_version")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.3")

    // Retrofit with Scalar Converter
    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")

    // Kotlin Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")

    // Retrofit with Jakewharton Converter
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")

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
