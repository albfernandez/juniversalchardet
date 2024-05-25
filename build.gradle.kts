plugins {
    id("com.android.library") version "8.4.0"
}

android {
    namespace = "org.mozilla.universalchardet"
    compileSdk = 34

    defaultConfig {
        minSdk = 19
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_7
        targetCompatibility = JavaVersion.VERSION_17
    }
}