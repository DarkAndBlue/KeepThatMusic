package de.darkandblue.keepthatmusic.mixin;

import de.darkandblue.keepthatmusic.KeepThatMusic;
import net.minecraft.sound.MusicSound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MusicSound.class)
public class MusicSoundMixin {
  @Inject(method = "shouldReplaceCurrentMusic", at = @At("HEAD"), cancellable = true)
  public void shouldReplaceCurrentMusic(CallbackInfoReturnable<Boolean> ci) {
    if (KeepThatMusic.DEBUG) {
      System.out.println("Set replace music false " + Math.random());
    }
    ci.setReturnValue(false);
    ci.cancel();
  }
}