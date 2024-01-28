package de.darkandblue.keepthatmusic.mixin;

import de.darkandblue.keepthatmusic.KeepThatMusic;
import net.minecraft.sound.MusicSound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MusicSound.class)
public class MixinMusicSound {
  /**
   * When the current music location doesn't fit your new music location it will be replaced
   * For example when switching from title screen to ingame
   * Forge-Mappings: Music.replaceCurrentMusic()
   */
  @Inject(method = "shouldReplaceCurrentMusic", at = @At("HEAD"), cancellable = true)
  public void shouldReplaceCurrentMusic(CallbackInfoReturnable<Boolean> ci) {
    if (KeepThatMusic.DEBUG) {
//      System.out.println("MusicSound.shouldReplaceCurrentMusic() " + Math.random());
    }
    ci.setReturnValue(false);
    ci.cancel();
  }
  
  @Inject(method = "getMaxDelay", at = @At("HEAD"), cancellable = true)
  public void getMaxDelay(CallbackInfoReturnable<Integer> ci) {
    if (KeepThatMusic.DEBUG) {
//      System.out.println("MusicSound.getMaxDelay() " + Math.random());
    }
    if(KeepThatMusic.MAX_MUSIC_DELAY != -1) {
      ci.setReturnValue(KeepThatMusic.MAX_MUSIC_DELAY);
      ci.cancel();
    }
  }
}