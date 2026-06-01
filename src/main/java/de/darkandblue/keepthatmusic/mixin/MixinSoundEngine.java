package de.darkandblue.keepthatmusic.mixin;

import de.darkandblue.keepthatmusic.duck.SelectiveSoundStopper;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.ChannelAccess;
import net.minecraft.client.sounds.SoundEngine;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.Map;

/**
 * Adds a "stop everything except this one music instance" capability to the sound engine. This is
 * the heart of the fix for the old behaviour where leaving a world kept <em>every</em> sound alive
 * (and leaked the orphaned sources). We reuse the engine's own per-instance stop, so channels are
 * properly freed.
 */
@Mixin(SoundEngine.class)
public abstract class MixinSoundEngine implements SelectiveSoundStopper {
  @Final
  @Shadow
  private Map<SoundInstance, ChannelAccess.ChannelHandle> instanceToChannel;

  @Shadow
  public abstract void stop(SoundInstance sound);

  @Override
  public void keepThatMusic$stopAllExcept(SoundInstance keep) {
    // Copy first: stop(...) mutates instanceToChannel.
    for (SoundInstance instance : new ArrayList<>(instanceToChannel.keySet())) {
      if (instance != keep) {
        stop(instance);
      }
    }
  }
}
