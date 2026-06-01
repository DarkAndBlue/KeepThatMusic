package de.darkandblue.keepthatmusic;

import de.darkandblue.keepthatmusic.config.KeepThatMusicConfig;
import net.fabricmc.api.ClientModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class KeepThatMusic implements ClientModInitializer {
  // log4j2 is Minecraft's logging backend on every supported version (SLF4J is not on the
  // 1.16.5 dev compile classpath), so we use it directly to stay version-agnostic.
  public static final Logger LOGGER = LogManager.getLogger("KeepThatMusic");

  @Override
  public void onInitializeClient() {
    // Eagerly load (and create-if-missing) the config so the file exists on first launch.
    KeepThatMusicConfig.get();
  }

  /** Convenience accessor used by the mixins to gate their behaviour. */
  public static KeepThatMusicConfig config() {
    return KeepThatMusicConfig.get();
  }

  public static void trace(String message) {
    if (config().debug) {
      LOGGER.info("[KeepThatMusic] {}", message);
    }
  }
}
