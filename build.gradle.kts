plugins {
    // Applies the correct Loom variant for the active Minecraft version.
    id("dev.kikugie.loom-back-compat")
}

// DO NOT set group = ...! Loom/Stonecutter manage coordinates.
// sc.properties[...] is a generic getter; assign to typed vals so Kotlin can infer String.
val modId: String = sc.properties["mod.id"]
val modName: String = sc.properties["mod.name"]
val modVersion: String = sc.properties["mod.version"]
val modMcCompat: String = sc.properties["mod.mc_compat"]
val fabricLoader: String = sc.properties["deps.fabric_loader"]

version = "$modVersion+${sc.current.version}"
base.archivesName = modId

val requiredJava: JavaVersion = when {
    sc.current.parsed >= "26.1" -> JavaVersion.VERSION_25
    sc.current.parsed >= "1.20.5" -> JavaVersion.VERSION_21
    sc.current.parsed >= "1.18" -> JavaVersion.VERSION_17
    sc.current.parsed >= "1.17" -> JavaVersion.VERSION_16
    else -> JavaVersion.VERSION_1_8
}

repositories {
    // Mod Menu is fetched from Modrinth's maven. maven.terraformersmc.com sits behind a CDN that
    // intermittently returns HTTP 400 to Gradle (works in a browser), which fails CI at random;
    // Modrinth's maven is reliable. Scoped to its own group so it isn't probed for anything else.
    exclusiveContent {
        forRepository { maven("https://api.modrinth.com/maven") { name = "Modrinth" } }
        filter { includeGroup("maven.modrinth") }
    }
}

dependencies {
    minecraft("com.mojang:minecraft:${sc.current.version}")
    // Applies Mojang Mappings, including on the obfuscated (pre-26.1) versions.
    loomx.applyMojangMappings()

    modImplementation("net.fabricmc:fabric-loader:$fabricLoader")

    // Mod Menu is an optional dependency. We only need its API to compile the
    // integration; pulled non-transitively so it does not drag in Fabric API.
    val modmenu = sc.properties.rawOrNull("deps", "modmenu")
    if (modmenu != null) {
        modCompileOnly("maven.modrinth:modmenu:$modmenu") { isTransitive = false }
    }
}

loom {
    runConfigs.all {
        runDir = "../../run" // Share the run directory between versions.
    }
}

java {
    withSourcesJar()
    targetCompatibility = requiredJava
    sourceCompatibility = requiredJava

    toolchain {
        vendor = JvmVendorSpec.ADOPTIUM
        languageVersion = JavaLanguageVersion.of(requiredJava.majorVersion)
    }
}

tasks {
    processResources {
        val props = mapOf(
            "id" to modId,
            "name" to modName,
            "version" to modVersion,
            "minecraft" to modMcCompat,
        )
        props.forEach { (k, v) -> inputs.property(k, v) }
        filesMatching("fabric.mod.json") { expand(props) }

        val mixinJava = "JAVA_${requiredJava.majorVersion}"
        inputs.property("java", mixinJava)
        filesMatching("*.mixins.json") { expand("java" to mixinJava) }
    }

    // Collects every built version into build/libs/<mod version>/ for distribution.
    register<Copy>("buildAndCollect") {
        group = "build"
        from(loomx.modJar.map { it.archiveFile }, loomx.modSourcesJar.map { it.archiveFile })
        into(rootProject.layout.buildDirectory.file("libs/$modVersion"))
        dependsOn("build")
    }
}
