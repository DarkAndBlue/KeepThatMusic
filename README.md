# Keep That Music

![](.github/icon.png)

* ### [🔗 Modrinth Page](https://modrinth.com/mod/keepthatmusic)
* ### [🔗 CurseForge Page](https://www.curseforge.com/minecraft/mc-mods/keep-that-music)

## What does this mod do?
This mod stops Minecraft from interrupting music when you switch worlds, or go back to title screen.
When you *do* leave a world, only the **music** keeps playing — every other sound is stopped normally.

## Configuration
Open **Mod Menu → Keep That Music** to:
- Enable / disable the mod (vanilla behaviour is fully restored when disabled)
- Override the maximum delay between music tracks

Settings are saved to `config/keepthatmusic.json`.

## Supported versions
A single codebase is built for multiple Minecraft versions via
[Stonecutter](https://stonecutter.kikugie.dev/): **1.16.5, 1.18.2, 1.19.2, 1.20.1, 1.21.1, 1.21.11
and 26.1.2+**. From 26.1 onward Minecraft ships unobfuscated, so all versions are built against
Mojang mappings.

Minimum Java is **8** (Minecraft 1.16's runtime); newer versions require newer Java (17 for
1.18–1.20.4, 21 for 1.21.x, 25 for 26.1), handled automatically per build.

### Building
```
./gradlew chiseledBuild            # build every supported version into build/libs/<version>/
./gradlew ":1.21.1:build"          # build a single version
./gradlew ":1.21.1:runClient"      # launch the game on a version to test in-game
```

### Testing
`.github/workflows/ci.yml` runs on every push/PR:
- **build** — compiles + processes mixins + remaps each of the 7 versions.
- **runtime** — boots the real client headlessly via
  [MC-Runtime-Test](https://github.com/headlesshq/mc-runtime-test) (HeadlessMC + Xvfb): joins a
  single-player world, then quits. This catches runtime mixin-apply failures that a compile check
  cannot, and exercises the mod's disconnect/shutdown paths.