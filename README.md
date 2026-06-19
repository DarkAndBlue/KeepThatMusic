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
A single codebase (via [Stonecutter](https://stonecutter.kikugie.dev/)) produces **four jars**,
each covering a *range* of Minecraft versions — because most of the game internals the mod touches
are stable across versions (Fabric intermediary mappings bridge them):

| Jar (build target) | Covers | Config screen | Mappings |
|---|---|---|---|
| `1.16.5` (legacy)  | `>=1.16 <1.21.6` | no (JSON file only) | intermediary |
| `1.21.11`          | `>=1.21.6 <1.22` | yes | intermediary |
| `26.1`             | `~26.1` (unobfuscated) | yes | official Mojang |
| `26.2`             | `~26.2` (unobfuscated) | yes | official Mojang |

### Building / running all tests
```
./gradlew buildAll                 # build all jars (compile + mixins + remap)
./gradlew ":1.16.5:build"          # build a single target
./gradlew ":1.21.11:runClient"     # launch the game on a target to test in-game
```
In the IntelliJ Gradle panel, `buildAll` is under the root project's **build** group, and each
target's `runClient` is under that subproject's **fabric** group. The headless runtime tests are
**not** Gradle tasks — they live in `.github/workflows/ci.yml` and run on GitHub Actions.

### Downloadable jars
`.github/workflows/artifacts.yml` builds the three final mod jars and uploads them as
GitHub Actions artifacts (one per build target, sources excluded). Download them from the **Actions** tab
→ pick a "Build Artifacts" run → **Artifacts** section at the bottom of the summary. Runs on pushes
to `master`, on `v*` tags, or manually via **Run workflow**. It publishes nowhere external — add a
publish job to that file later for Modrinth/CurseForge/Releases.