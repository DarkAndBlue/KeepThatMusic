//? if modmenu {
package de.darkandblue.keepthatmusic.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

/**
 * Registers the config screen with Mod Menu. Only compiled on nodes that pin a Mod Menu build
 * (the {@code modmenu} Stonecutter constant); harmless if Mod Menu is not installed at runtime
 * because the loader simply ignores the entrypoint.
 */
public class ModMenuIntegration implements ModMenuApi {
  @Override
  public ConfigScreenFactory<?> getModConfigScreenFactory() {
    return KeepThatMusicConfigScreen::new;
  }
}
//?}
