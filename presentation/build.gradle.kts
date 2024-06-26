plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.hilt)
}

android {
    buildFeatures {
        viewBinding = true
        compose = true
    }

    namespace = "ru.gozerov.presentation"
    compileSdk = 34

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.11"
    }
}

dependencies {
    implementation(project(path = ":domain"))

    //Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //Hilt
    implementation(libs.dagger.hilt)
    kapt(libs.dagger.hilt.compiler)

    //Navigation
    implementation(libs.navigation.compose)
    implementation(libs.hilt.navigation)

    //Jetpack Compose
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.compose.activity)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui.viewbinding)

    //Coil
    implementation(libs.coil)

    //Permissions
    implementation(libs.permissions)

    //SystemUiController
    implementation(libs.ui.controller)

    //Calendar
    implementation(libs.calendar)
    implementation(libs.calendar.kotlin.datetime)

    //Paging
    implementation(libs.paging.runtime)
    implementation(libs.paging.compose)

    //Graphics
    implementation(libs.graphics)

}