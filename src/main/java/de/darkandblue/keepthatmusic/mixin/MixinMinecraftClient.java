package de.darkandblue.keepthatmusic.mixin;

import de.darkandblue.keepthatmusic.KeepThatMusic;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.MusicTracker;
import net.minecraft.client.sound.SoundManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MixinMinecraftClient {
  
  @Shadow
  private boolean paused;
  
  @Final
  @Shadow
  private MusicTracker musicTracker;
  
  @Redirect(method = "reset", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sound/SoundManager;stopAll()V"))
  private void reset(SoundManager instance) {
    if(KeepThatMusic.DEBUG) {
      System.out.println("MinecraftClient().reset.stopAll() " + Math.random());
    }
  }
  
  @Inject(method = "tick", at = @At(value = "RETURN"))
  private void tick(CallbackInfo ci) {
    if(KeepThatMusic.DEBUG) {
//      System.out.println("MinecraftClient().tick() " + Math.random());
    }
    
    if(paused) {
      this.musicTracker.tick();
    }
  }
}