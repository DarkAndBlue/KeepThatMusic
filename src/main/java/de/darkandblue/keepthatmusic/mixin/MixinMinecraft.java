package de.darkandblue.keepthatmusic.mixin;

import de.darkandblue.keepthatmusic.KeepThatMusic;
import net.minecraft.client.Minecraft;
import net.minecraft.client.sounds.MusicManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Keeps the music manager ticking while the game is paused, so single-player pause-menu music keeps
 * advancing instead of stalling. (Mojang name: Minecraft.)
 */
@Mixin(Minecraft.class)
public abstract class MixinMinecraft {
  @Shadow
  private boolean pause;

  @Shadow
  public abstract MusicManager getMusicManager();

  @Inject(method = "tick", at = @At("RETURN"))
  private void keepThatMusic$tick(CallbackInfo ci) {
    if (KeepThatMusic.config().enabled && pause) {
      getMusicManager().tick();
    }
  }
}
