package de.darkandblue.keepthatmusic.mixin;

import de.darkandblue.keepthatmusic.KeepThatMusic;
import de.darkandblue.keepthatmusic.config.KeepThatMusicConfig;
import de.darkandblue.keepthatmusic.duck.MusicHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.MusicManager;
import net.minecraft.client.sounds.SoundManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Keeps the music manager ticking while the game is paused (so pause-menu music keeps advancing),
 * and drives the optional "stop music on world join / on return to menu" behaviour by watching the
 * client level appear/disappear. (Mojang name: Minecraft.)
 */
@Mixin(Minecraft.class)
public abstract class MixinMinecraft {
  @Shadow
  private boolean pause;

  @Shadow
  public ClientLevel level;

  @Shadow
  public abstract MusicManager getMusicManager();

  @Shadow
  public abstract SoundManager getSoundManager();

  @Unique
  private boolean keepThatMusic$wasInWorld;

  @Inject(method = "tick", at = @At("RETURN"))
  private void keepThatMusic$tick(CallbackInfo ci) {
    KeepThatMusicConfig config = KeepThatMusic.config();
    boolean inWorld = this.level != null;

    if (config.enabled) {
      if (pause) {
        getMusicManager().tick();
      }
      // A world just loaded (menu -> game): the music still playing was the menu music we kept.
      if (inWorld && !keepThatMusic$wasInWorld && config.stopMenuMusicOnJoin) {
        keepThatMusic$stopCurrentMusic("join");
      // Returned to the main menu (game -> menu): stop the music we kept playing.
      } else if (!inWorld && keepThatMusic$wasInWorld && config.stopMusicOnReturnToMenu) {
        keepThatMusic$stopCurrentMusic("menu");
      }
    }

    keepThatMusic$wasInWorld = inWorld;
  }

  @Unique
  private void keepThatMusic$stopCurrentMusic(String where) {
    MusicManager musicManager = getMusicManager();
    SoundInstance music = ((MusicHolder) musicManager).keepThatMusic$getCurrentMusic();
    if (music != null) {
      getSoundManager().stop(music);
      // Clear it so the manager starts the appropriate fresh music on its next tick.
      ((MusicHolder) musicManager).keepThatMusic$setCurrentMusic(null);
      KeepThatMusic.trace("Stopped kept music on " + where);
    }
  }
}
