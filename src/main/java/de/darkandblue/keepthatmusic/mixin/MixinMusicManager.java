package de.darkandblue.keepthatmusic.mixin;

import de.darkandblue.keepthatmusic.KeepThatMusic;
import de.darkandblue.keepthatmusic.duck.MusicHolder;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.MusicManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Stops the music manager from killing the current track on world/dimension/title transitions, and
 * exposes the current music instance so other mixins can keep it alive while stopping everything
 * else. (Forge/Mojang name: MusicManager#stopPlaying.)
 */
@Mixin(MusicManager.class)
public class MixinMusicManager implements MusicHolder {
  @Shadow
  private SoundInstance currentMusic;

  @Inject(method = "stopPlaying()V", at = @At("HEAD"), cancellable = true)
  private void keepThatMusic$stopPlaying(CallbackInfo ci) {
    if (KeepThatMusic.config().enabled) {
      KeepThatMusic.trace("MusicManager.stopPlaying() cancelled");
      ci.cancel();
    }
  }

  @Override
  public SoundInstance keepThatMusic$getCurrentMusic() {
    return currentMusic;
  }

  @Override
  public void keepThatMusic$setCurrentMusic(SoundInstance music) {
    this.currentMusic = music;
  }
}
