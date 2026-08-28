plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.norman.room"
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

ksp {
    // 匯出 schema JSON，否則 Room 每次編譯都會警告 schema 未匯出。
    // 這批 JSON 進版控後，日後改 entity 時 diff 看得到 schema 變化。
    arg("room.schemaLocation", "$projectDir/schemas")
}

dependencies {

    coreLibraryDesugaring(libs.desugar.jdk.libs)

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    implementation(project(path = ":core:model"))

    testImplementation(libs.junit)
}
