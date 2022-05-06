package de.darkandblue.keepthatmusic.mixin;

import de.darkandblue.keepthatmusic.KeepThatMusic;
import net.minecraft.client.sound.MusicTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MusicTracker.class)
public class MusicTrackerMixin {
  @Inject(method = "stop", at = @At("HEAD"), cancellable = true)
  public void interruptStopMusicOnWorldSwitch(CallbackInfo ci) {
    if (KeepThatMusic.DEBUG) {
      System.out.println("Stop music " + Math.random());
    }
    ci.cancel();
  }
}