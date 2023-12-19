plugins {
    kotlin("multiplatform")
    alias(libs.plugins.kotlin.compose)
}

val kotlinVersion: String by project
val composeKotlinVersion: String by project

kotlin {

    js(IR) { browser() }

    sourceSets {

        all {
            languageSettings {
                languageVersion = "2.0"
            }
        }

        commonMain {
            dependencies {
                implementation(project(":doc"))
                implementation(compose.runtime)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.atomicfu)
                implementation(libs.kotlinx.atomicfu.runtime)
            }
        }

        jsMain {
            dependencies {
                implementation(compose.html.core)
                implementation(npm("highlight.js", "11.9.0"))
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
