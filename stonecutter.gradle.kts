plugins {
    id("dev.kikugie.stonecutter")
}

// Node iterated against by default in the IDE / single-version Gradle tasks.
stonecutter active "1.21.11"

// Aggregate task so it's discoverable in the IDE Gradle panel (root project > "build" group)
// and runnable as `./gradlew buildAll`: builds every supported Minecraft version at once.
tasks.register("buildAll") {
    group = "build"
    description = "Builds (compile + mixins + remap) every supported Minecraft version."
    dependsOn(subprojects.map { "${it.path}:build" })
}

// See https://stonecutter.kikugie.dev/wiki/config/params
stonecutter parameters {
    // Enables the Mod Menu integration only on nodes that pin a `deps.modmenu` build.
    constants["modmenu"] = node.project.findProperty("deps.modmenu") != null
}
