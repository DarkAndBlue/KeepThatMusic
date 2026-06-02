# Keep That Music

![](.github/icon.png)

* ### [🔗 Modrinth Page](https://modrinth.com/mod/keepthatmusic)
* ### [🔗 CurseForge Page](https://www.curseforge.com/minecraft/mc-mods/keep-that-music) (outdated)

## What does this mod do?
This mod stops Minecraft from interrupting music when you switch worlds, or go back to title screen.
When you *do* leave a world, only the **music** keeps playing — every other sound is stopped normally.

## Configuration
Open **Mod Menu → Keep That Music** to:
- Enable / disable the mod (vanilla behaviour is fully restored when disabled)
- Override the maximum delay between music tracks

Settings are saved to `config/keepthatmusic.json`.

## Supported versions
A single codebase (via [Stonecutter](https://stonecutter.kikugie.dev/)) produces **three jars**,
each covering a *range* of Minecraft versions — because most of the game internals the mod touches
are stable across versions (Fabric intermediary mappings bridge them):

| Jar (build target) | Covers | Config screen | Mappings |
|---|---|---|---|
| `1.16.5` (legacy)  | `>=1.16 <1.21.6` | no (JSON file only) | intermediary |
| `1.21.11`          | `>=1.21.6 <1.22` | yes | intermediary |
| `26.1`             | `~26.1` (unobfuscated) | yes | official Mojang |

The legacy jar is built at Java 8 with no config screen, so nothing forces it to split across the
old range. The splits exist only where the game genuinely changed: `Music` became a record
(`getMaxDelay`→`maxDelay`) in 1.21.6, `SoundManager.pause()` was removed in 1.21.11, and 26.1 dropped
obfuscation entirely. Minimum Java is **8** (Minecraft 1.16's runtime).

### Building / running all tests
```
./gradlew buildAll                 # build all three jars (compile + mixins + remap)
./gradlew ":1.16.5:build"          # build a single target
./gradlew ":1.21.11:runClient"     # launch the game on a target to test in-game
```
In the IntelliJ Gradle panel, `buildAll` is under the root project's **build** group, and each
target's `runClient` is under that subproject's **fabric** group. The headless runtime tests are
**not** Gradle tasks — they live in `.github/workflows/ci.yml` and run on GitHub Actions.

### Testing
`.github/workflows/ci.yml` runs on every push/PR:
- **build** — compiles + processes mixins + remaps each of the three jars.
- **runtime** — boots the real client headlessly via
  [MC-Runtime-Test](https://github.com/headlesshq/mc-runtime-test) (HeadlessMC + Xvfb): joins a
  single-player world, runs `/playsound`, then quits. The legacy jar is run against one
  representative version from **every generation** in its range (1.18.2, 1.19.4, 1.20.1, 1.20.3,
  1.20.6, 1.21.1) so a break at any minor update is caught — testing literally every patch is
  unnecessary because the few internals the mod touches only change at notable refactors. This
  catches runtime mixin-apply failures a compile check cannot.

`.github/workflows/full-version-check.yml` is a **manual** (`workflow_dispatch`) exhaustive check:
it boots the correct jar headlessly on **every** supported Minecraft version across 1.16→26.1.x
(~26 jobs) — a mixin-apply error crashes startup and fails that version's job. Run it from
**Actions → Full Version Check → Run workflow** before a release. It's manual because spinning up
~26 headless clients is too heavy for every commit.

### Downloadable jars
`.github/workflows/artifacts.yml` builds the three final mod jars and uploads them as
GitHub Actions artifacts (one per build target, sources excluded). Download them from the **Actions** tab
→ pick a "Build Artifacts" run → **Artifacts** section at the bottom of the summary. Runs on pushes
to `master`, on `v*` tags, or manually via **Run workflow**. It publishes nowhere external — add a
publish job to that file later for Modrinth/CurseForge/Releases.
