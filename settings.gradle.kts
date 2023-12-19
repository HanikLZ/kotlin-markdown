pluginManagement {

    val kotlinVersion: String by settings

    repositories {
        mavenLocal()
        gradlePluginPortal()
        mavenCentral()
    }
    plugins {
        kotlin("multiplatform") version kotlinVersion
        kotlin("plugin.serialization") version kotlinVersion
    }
}

dependencyResolutionManagement {

    val kotlinxSerializationVersion = "1.6.2"
    val kotlinxCoroutinesVersion = "1.7.3"
    val kotlinxAtomicfuVersion = "0.23.1"
    val kotlinVersion: String by settings

    versionCatalogs {

        fun VersionCatalogBuilder.kotlinx(name: String, version: String, prefix: String? = "kotlinx") =
            library("kotlinx-$name", "org.jetbrains.kotlinx:${if (prefix != null) "$prefix-$name" else name}:$version")

        fun VersionCatalogBuilder.kotlinxSerialization(vararg name: String, version: String = kotlinxSerializationVersion) = name.forEach {
            kotlinx("serialization-$it", version)
        }

        create("libs") {
            plugin("kotlin-compose", "org.jetbrains.compose").version("1.5.11")
            kotlinxSerialization("json", "protobuf")
            kotlinx("coroutines-core", kotlinxCoroutinesVersion)
            kotlinx("atomicfu", kotlinxAtomicfuVersion, null)
            library("kotlinx-atomicfu-runtime", "org.jetbrains.kotlin:kotlinx-atomicfu-runtime:$kotlinVersion")
        }

    }
}

include(":doc", ":compose-html", ":compose-html-example")
