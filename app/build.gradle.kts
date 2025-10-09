import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

val apiKey: String = run {
    val propsFile = rootProject.file("local.properties")
    if (propsFile.exists()) {
        val props = Properties().apply { load(propsFile.inputStream()) }
        props.getProperty("API_KEY") ?: ""
    } else ""
}

android {
    namespace = "com.nb.coininfo"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.nb.coininfo"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        buildConfigField("String", "API_KEY", "\"$apiKey\"")

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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }


    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/versions/9/OSGI-INF/MANIFEST.MF"
            excludes += setOf(
//                "fonts/NotoSansCJK-*.ttc",
//                "fonts/NotoColorEmoji.ttf",
//                "fonts/DroidSansFallback.ttf",
//                "res/font/NotoSansCJK-*.ttc",
//                "res/font/NotoColorEmoji.ttf",
                "/fonts/**"
            )
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    //implementation(libs.androidx.compose.testing)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    //debugImplementation(libs.androidx.ui.tooling)
    //debugImplementation(libs.androidx.ui.test.manifest)


    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    ksp(libs.hilt.compiler)


    /*
    * Networking
    * */
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.retrofit2.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.gson)
    implementation(platform(libs.okhttp.bom))
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)


    implementation(libs.lottie.compose)
    implementation(libs.androidx.navigation.compose)


    implementation(libs.moshi.kotlin)
    ksp(libs.moshi.ksp)

    implementation("co.yml:ycharts:2.1.0")
    implementation("io.coil-kt:coil-compose:2.5.0")
    implementation("com.patrykandpatrick.vico:compose-m3:1.14.0")


    implementation("androidx.room:room-runtime:2.4.2")
    ksp("androidx.room:room-compiler:2.4.2")
    implementation("androidx.room:room-ktx:2.4.2")


}