package de.darkandblue.keepthatmusic.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.darkandblue.keepthatmusic.KeepThatMusic;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Plain config POJO persisted as JSON at {@code config/keepthatmusic.json}.
 *
 * <p>Every behaviour added by the mod's mixins is gated on {@link #enabled}, so the mod can be
 * turned fully off (reverting to vanilla behaviour) from the Mod Menu screen without uninstalling.
 */
public class KeepThatMusicConfig {
  private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
  private static final Path PATH =
      FabricLoader.getInstance().getConfigDir().resolve("keepthatmusic.json");

  private static KeepThatMusicConfig instance;

  /** Master switch. When false the mod does nothing and vanilla behaviour is restored. */
  public boolean enabled = true;

  /** Overrides {@code Music#getMaxDelay}. -1 keeps the vanilla per-biome delay. */
  public int maxMusicDelay = -1;

  /**
   * When true, the (menu) music that was playing is stopped on joining a world, instead of being
   * kept. Default false: menu music keeps playing in-game until the player opts in.
   */
  public boolean stopMenuMusicOnJoin = false;

  /**
   * When true, the kept music is stopped on returning to the main menu. Default false: the music
   * keeps playing on the title screen.
   */
  public boolean stopMusicOnReturnToMenu = false;

  /** Prints mixin tracing to the log. */
  public boolean debug = false;

  public static KeepThatMusicConfig get() {
    if (instance == null) {
      instance = load();
    }
    return instance;
  }

  private static KeepThatMusicConfig load() {
    if (Files.exists(PATH)) {
      try {
        KeepThatMusicConfig loaded = GSON.fromJson(Files.newBufferedReader(PATH), KeepThatMusicConfig.class);
        if (loaded != null) {
          return loaded;
        }
      } catch (IOException | RuntimeException e) {
        KeepThatMusic.LOGGER.warn("[KeepThatMusic] Could not read config, using defaults", e);
      }
    }
    KeepThatMusicConfig fresh = new KeepThatMusicConfig();
    fresh.save();
    return fresh;
  }

  public void save() {
    try {
      Files.createDirectories(PATH.getParent());
      Files.write(PATH, GSON.toJson(this).getBytes());
    } catch (IOException e) {
      KeepThatMusic.LOGGER.warn("[KeepThatMusic] Could not write config", e);
    }
  }
}
