import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.hilt)
}

android {
    namespace = "ru.gozerov.fitladya"
    compileSdk = 34

    defaultConfig {
        applicationId = "ru.gozerov.fitladya"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val vkIDClientId = project.findProperty("VKIDClientID") as String
        val vkIDClientSecret = project.findProperty("VKIDClientSecret") as String
        val vkIDRedirectHost = project.findProperty("VKIDRedirectHost") as String
        val vkIDRedirectScheme = project.findProperty("VKIDRedirectScheme") as String

        addManifestPlaceholders(
            mapOf(
                "VKIDClientID" to vkIDClientId, // ID вашего приложения (app_id).
                "VKIDClientSecret" to vkIDClientSecret, // Ваш защищенный ключ (client_secret).
                "VKIDRedirectHost" to vkIDRedirectHost, // Обычно используется vk.com.
                "VKIDRedirectScheme" to vkIDRedirectScheme, // Обычно используется vk{ID приложения}.
            )
        )
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
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")
    implementation(project(path = ":presentation"))
    implementation(project(path = ":domain"))
    implementation(project(path = ":data"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)

    //Hilt
    implementation(libs.dagger.hilt)
    kapt(libs.dagger.hilt.compiler)

    //Retrofit2 & OkHttp3
    implementation(libs.retrofit2)
    implementation(libs.retrofit.result.adapter)
    implementation(libs.moshi.converter)
    implementation(platform(libs.okhttp3.bom))
    implementation(libs.okhttp3)
    implementation(libs.okhttp3.logging.interceptor)

    //VK
    implementation(libs.vk.id.sdk)
    implementation(libs.vk.id.onetap)

}