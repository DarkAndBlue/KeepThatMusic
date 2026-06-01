package de.darkandblue.keepthatmusic.mixin;

import de.darkandblue.keepthatmusic.KeepThatMusic;
import de.darkandblue.keepthatmusic.duck.MusicHolder;
import de.darkandblue.keepthatmusic.duck.SelectiveSoundStopper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.MusicManager;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.client.sounds.SoundManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * The behavioural core.
 *
 * <ul>
 *   <li>{@code stop()} (stop-all, fired when leaving a world/server) is redirected to stop every
 *       sound <em>except</em> the current music — fixing both "all sounds keep playing" and the
 *       orphaned-source leak.
 *   <li>{@code pause()} (pause menu / focus loss) is cancelled so the music keeps playing.
 *   <li>{@code destroy()} (game shutdown) is intentionally <em>not</em> touched, so the sound
 *       engine is freed normally — this is what fixes the crash on game close.
 * </ul>
 */
@Mixin(SoundManager.class)
public class MixinSoundManager {
  @Final
  @Shadow
  private SoundEngine soundEngine;

  @Inject(method = "stop()V", at = @At("HEAD"), cancellable = true)
  private void keepThatMusic$stopAll(CallbackInfo ci) {
    if (!KeepThatMusic.config().enabled) {
      return;
    }
    ((SelectiveSoundStopper) soundEngine).keepThatMusic$stopAllExcept(currentMusic());
    KeepThatMusic.trace("SoundManager.stop() -> kept music, stopped everything else");
    ci.cancel();
  }

  // SoundManager.pause() was removed in 1.21.11 in favour of pauseAllExcept(...), which already
  // keeps music playing through a pause. Only inject on versions that still have pause().
  //? if <1.21.11 {
  /*@Inject(method = "pause()V", at = @At("HEAD"), cancellable = true)
  private void keepThatMusic$pause(CallbackInfo ci) {
    if (KeepThatMusic.config().enabled) {
      KeepThatMusic.trace("SoundManager.pause() cancelled");
      ci.cancel();
    }
  }
  *///?}

  private static SoundInstance currentMusic() {
    MusicManager musicManager = Minecraft.getInstance().getMusicManager();
    return ((MusicHolder) musicManager).keepThatMusic$getCurrentMusic();
  }
}
