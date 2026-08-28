plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.norman.repository"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        // minSdk 24 需要 desugaring 才能使用 java.time（解析 ISO-8601 時間戳）
        isCoreLibraryDesugaringEnabled = true
    }

}

dependencies {

    coreLibraryDesugaring(libs.desugar.jdk.libs)

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    implementation(project(path = ":core:model"))
    implementation(project(path = ":core:retrofit"))
    implementation(project(path = ":core:room"))

    testImplementation(libs.junit)
}