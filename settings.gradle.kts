pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.fabricmc.net/")
        maven("https://maven.kikugie.dev/releases") { name = "KikuGie Releases" }
        maven("https://maven.kikugie.dev/snapshots") { name = "KikuGie Snapshots" }
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.9.4"
    id("dev.kikugie.loom-back-compat") version "0.3"
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

stonecutter {
    create(rootProject) {
        // Obfuscated (Yarn-era) releases — Mojang mappings are layered on by Loom.
        // See https://stonecutter.kikugie.dev/wiki/start/#choosing-minecraft-versions
        versions("1.16.5", "1.18.2", "1.19.2", "1.20.1", "1.21.1", "1.21.11")
        // De-obfuscated releases ship Mojang mappings natively.
        version("26.1", "26.1.2")
        // Default node to develop/iterate against.
        vcsVersion = "1.21.1"
    }
}

rootProject.name = "keepthatmusic"
