//? if >=1.20 {
package de.darkandblue.keepthatmusic.modmenu;

import de.darkandblue.keepthatmusic.config.KeepThatMusicConfig;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/**
 * Hand-built settings screen (no ClothConfig dependency). Only shipped on modern versions
 * (>=1.20), where the GUI API is stable (Component.literal / Button.builder / addRenderableWidget),
 * so it needs no per-version branching. On the legacy jar there is no screen — the mod is
 * configured via {@code config/keepthatmusic.json}.
 */
public class KeepThatMusicConfigScreen extends Screen {
  private final Screen parent;
  private final KeepThatMusicConfig config = KeepThatMusicConfig.get();

  public KeepThatMusicConfigScreen(Screen parent) {
    super(Component.literal("Keep That Music"));
    this.parent = parent;
  }

  @Override
  protected void init() {
    int x = this.width / 2 - 100;
    int y = this.height / 4;

    addRenderableWidget(Button.builder(enabledLabel(), b -> {
      config.enabled = !config.enabled;
      b.setMessage(enabledLabel());
    }).bounds(x, y, 200, 20).build());

    addRenderableWidget(Button.builder(delayLabel(), b -> {
      config.maxMusicDelay = cycleDelay(config.maxMusicDelay);
      b.setMessage(delayLabel());
    }).bounds(x, y + 24, 200, 20).build());

    addRenderableWidget(Button.builder(joinLabel(), b -> {
      config.stopMenuMusicOnJoin = !config.stopMenuMusicOnJoin;
      b.setMessage(joinLabel());
    }).bounds(x, y + 48, 200, 20).build());

    addRenderableWidget(Button.builder(menuLabel(), b -> {
      config.stopMusicOnReturnToMenu = !config.stopMusicOnReturnToMenu;
      b.setMessage(menuLabel());
    }).bounds(x, y + 72, 200, 20).build());

    addRenderableWidget(Button.builder(Component.literal("Done"), b -> onClose())
        .bounds(x, y + 108, 200, 20).build());
  }

  private Component enabledLabel() {
    return Component.literal("Mod: " + (config.enabled ? "Enabled" : "Disabled"));
  }

  private Component delayLabel() {
    String value = config.maxMusicDelay == -1 ? "Vanilla" : config.maxMusicDelay + " ticks";
    return Component.literal("Max music delay: " + value);
  }

  private Component joinLabel() {
    return Component.literal("Stop menu music on join: " + (config.stopMenuMusicOnJoin ? "Yes" : "No"));
  }

  private Component menuLabel() {
    return Component.literal("Stop music on return to menu: " + (config.stopMusicOnReturnToMenu ? "Yes" : "No"));
  }

  /** Cycles through a few useful presets: Vanilla -> 0 -> 100 -> 1200 -> Vanilla. */
  private static int cycleDelay(int current) {
    switch (current) {
      case -1: return 0;
      case 0: return 100;
      case 100: return 1200;
      default: return -1;
    }
  }

  @Override
  public void onClose() {
    config.save();
    if (minecraft != null) {
      minecraft.setScreen(parent);
    }
  }
}
//?}
