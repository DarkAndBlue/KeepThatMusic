package de.darkandblue.keepthatmusic.modmenu;

import de.darkandblue.keepthatmusic.config.KeepThatMusicConfig;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/**
 * A small hand-built settings screen (no ClothConfig dependency, so it works uniformly across every
 * supported Minecraft version). Only {@link #init()} is overridden — the buttons carry their own
 * labels, so we avoid the version-churny {@code render(...)} signature entirely.
 *
 * <p>The three cross-version GUI differences (text factory, button factory, widget registration)
 * are isolated into the helpers below behind Stonecutter {@code //?} branches.
 */
public class KeepThatMusicConfigScreen extends Screen {
  private final Screen parent;
  private final KeepThatMusicConfig config = KeepThatMusicConfig.get();

  public KeepThatMusicConfigScreen(Screen parent) {
    super(literal("Keep That Music"));
    this.parent = parent;
  }

  @Override
  protected void init() {
    int x = this.width / 2 - 100;
    int y = this.height / 4;

    addWidget(makeButton(x, y, 200, 20, enabledLabel(), b -> {
      config.enabled = !config.enabled;
      b.setMessage(enabledLabel());
    }));

    addWidget(makeButton(x, y + 24, 200, 20, delayLabel(), b -> {
      config.maxMusicDelay = cycleDelay(config.maxMusicDelay);
      b.setMessage(delayLabel());
    }));

    addWidget(makeButton(x, y + 60, 200, 20, literal("Done"), b -> onClose()));
  }

  private Component enabledLabel() {
    return literal("Mod: " + (config.enabled ? "Enabled" : "Disabled"));
  }

  private Component delayLabel() {
    String value = config.maxMusicDelay == -1 ? "Vanilla" : config.maxMusicDelay + " ticks";
    return literal("Max music delay: " + value);
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

  // --- cross-version GUI helpers ------------------------------------------------------------

  /** Text component factory: {@code Component.literal} (1.19+) vs {@code new TextComponent} (older). */
  private static Component literal(String text) {
    //? if >=1.19 {
    return Component.literal(text);
    //?} else {
    /*return new net.minecraft.network.chat.TextComponent(text);*/
    //?}
  }

  /** Button factory: builder (1.20+) vs the legacy constructor (older). */
  private Button makeButton(int x, int y, int w, int h, Component msg, Button.OnPress onPress) {
    //? if >=1.20 {
    return Button.builder(msg, onPress).bounds(x, y, w, h).build();
    //?} else {
    /*return new Button(x, y, w, h, msg, onPress);*/
    //?}
  }

  /** Widget registration: {@code addRenderableWidget} (1.17+) vs {@code addButton} (1.16). */
  private void addWidget(Button button) {
    //? if >=1.17 {
    addRenderableWidget(button);
    //?} else {
    /*addButton(button);*/
    //?}
  }
}
