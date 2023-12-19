plugins {
    kotlin("multiplatform")
}

kotlin {
    js(IR) {
        nodejs()
        browser()
    }
/*    jvm()
    macosX64()
    macosArm64()
    linuxArm64()
    linuxX64()
    mingwX64()*/
    sourceSets {
        all {
            languageSettings {
                languageVersion = "2.0"
            }
        }
    }
}
