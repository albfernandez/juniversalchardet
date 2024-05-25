plugins {
    id("com.android.library") version "8.4.0"
}

android {
    namespace = "org.mozilla.universalchardet"
    compileSdk = 34

    defaultConfig {
        minSdk = 14
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_17
    }
}