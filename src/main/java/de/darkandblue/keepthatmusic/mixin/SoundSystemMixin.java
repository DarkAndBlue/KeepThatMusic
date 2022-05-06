package de.darkandblue.keepthatmusic.mixin;

import de.darkandblue.keepthatmusic.KeepThatMusic;
import net.minecraft.client.sound.SoundSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SoundSystem.class)
public class SoundSystemMixin {
  @Inject(method = "stopAll", at = @At("HEAD"), cancellable = true)
  public void shouldReplaceCurrentMusic(CallbackInfo ci) {
    if (KeepThatMusic.DEBUG) {
      System.out.println("Stop all " + Math.random());
    }
    ci.cancel();
  }
}