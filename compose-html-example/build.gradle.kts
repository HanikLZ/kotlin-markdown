plugins {
    kotlin("multiplatform")
    alias(libs.plugins.kotlin.compose)
}

val kotlinVersion: String by project
val composeKotlinVersion: String by project

kotlin {

    js(IR) {
        browser {
            commonWebpackConfig {
                cssSupport { enabled.set(true) }
                scssSupport { enabled.set(true) }
            }
        }
        binaries.executable()
    }

    sourceSets {

        all {
            languageSettings {
                languageVersion = "2.0"
            }
        }


        commonMain {
            dependencies {
                implementation(compose.runtime)
            }
        }

        jsMain {
            dependencies {
                implementation(compose.html.core)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.atomicfu)
                implementation(libs.kotlinx.atomicfu.runtime)
                implementation(project(":compose-html"))
            }
        }

    }
}

compose {
    if (composeKotlinVersion != kotlinVersion) {
        kotlinCompilerPlugin.set(dependencies.compiler.forKotlin(composeKotlinVersion))
        kotlinCompilerPluginArgs.add("suppressKotlinVersionCompatibilityCheck=$kotlinVersion")
    }
}
