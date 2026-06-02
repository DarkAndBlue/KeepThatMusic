package de.darkandblue.keepthatmusic.mixin;

import de.darkandblue.keepthatmusic.KeepThatMusic;
import de.darkandblue.keepthatmusic.config.KeepThatMusicConfig;
import net.minecraft.sounds.Music;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Prevents new locations (title screen, biome music, etc.) from replacing the track that is already
 * playing, and applies the configurable max-delay override. (Mojang name: Music.)
 */
@Mixin(Music.class)
public class MixinMusic {
  @Inject(method = "replaceCurrentMusic", at = @At("HEAD"), cancellable = true)
  private void keepThatMusic$replaceCurrentMusic(CallbackInfoReturnable<Boolean> cir) {
    if (KeepThatMusic.config().enabled) {
      cir.setReturnValue(false);
    }
  }

  // Music became a record in 1.21.5, renaming getMaxDelay() -> maxDelay(). (Before 1.21.5 it is a
  // regular class with stable intermediary method names, so the legacy jar spans 1.16–1.21.4.)
  //? if >=1.21.5 {
  @Inject(method = "maxDelay", at = @At("HEAD"), cancellable = true)
  //?} else {
  /*@Inject(method = "getMaxDelay", at = @At("HEAD"), cancellable = true)
  *///?}
  private void keepThatMusic$getMaxDelay(CallbackInfoReturnable<Integer> cir) {
    KeepThatMusicConfig config = KeepThatMusic.config();
    if (config.enabled && config.maxMusicDelay != -1) {
      cir.setReturnValue(config.maxMusicDelay);
    }
  }
}
