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
        // One build per distributable jar. Each jar covers a RANGE of Minecraft versions (declared
        // by mod.mc_compat in stonecutter.properties.toml), not a single version:
        //   1.16.5  -> built at Java 8, no config screen, runs across >=1.16 <1.21.2 via
        //              version-stable Fabric intermediary mappings (like the original mod did).
        //   1.21.11 -> the modern 1.21 line (Music record + pause() removal), with config screen.
        //   26.1    -> the un-obfuscated era (official mappings), with config screen.
        //   26.2    -> 26.2 moved screen-setting to Minecraft.gui.setScreen(); separate jar.
        versions("1.16.5", "1.21.11")
        version("26.1", "26.1.2")
        version("26.2", "26.2")
        // Default node to develop/iterate against (a modern node so the config screen is active).
        vcsVersion = "1.21.11"
    }
}

rootProject.name = "keepthatmusic"
