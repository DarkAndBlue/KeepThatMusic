plugins {
    id("dev.kikugie.stonecutter")
}

// Node iterated against by default in the IDE / single-version Gradle tasks.
stonecutter active "1.21.1"

// See https://stonecutter.kikugie.dev/wiki/config/params
stonecutter parameters {
    // Enables the Mod Menu integration only on nodes that pin a `deps.modmenu` build.
    constants["modmenu"] = node.project.findProperty("deps.modmenu") != null
}
