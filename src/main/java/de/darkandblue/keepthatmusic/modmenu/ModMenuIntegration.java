//? if modmenu {
package de.darkandblue.keepthatmusic.modmenu;

import com.terraformersmc.modmenu.api.ModMenuApi;

/**
 * Registers the mod with Mod Menu. Present on every version so the {@code modmenu} entrypoint in
 * fabric.mod.json always resolves. On modern versions (>=1.20) it returns the config screen; on the
 * legacy jar it falls back to Mod Menu's default (no config button — configure via the JSON file).
 */
public class ModMenuIntegration implements ModMenuApi {
  //? if >=1.20 {
  @Override
  public com.terraformersmc.modmenu.api.ConfigScreenFactory<?> getModConfigScreenFactory() {
    return KeepThatMusicConfigScreen::new;
  }
  //?}
}
//?}
