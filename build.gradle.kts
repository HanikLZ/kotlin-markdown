plugins {
    kotlin("multiplatform") apply false
}

allprojects {
    repositories {
        mavenLocal()
        mavenCentral()
        maven("https://jitpack.io")
        maven("https://dev.cqdune.com:8008/nexus/repository/mdvsc-releases")
        google()
    }
}

/*
plugins.apply {
    withType<NodeJsRootPlugin> { the<NodeJsRootExtension>().download = false }
    withType<YarnPlugin> { the<YarnRootExtension>().download = false }
} */
