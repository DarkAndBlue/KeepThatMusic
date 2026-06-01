package de.darkandblue.keepthatmusic.duck;

import net.minecraft.client.resources.sounds.SoundInstance;

/**
 * Duck interface implemented (via mixin) on the game's SoundEngine. Lets us stop every currently
 * playing sound <em>except</em> the music instance we want to keep — the core fix for "all sounds
 * keep playing when leaving a world".
 */
public interface SelectiveSoundStopper {
  /** Stops every active sound source whose instance is not {@code keep}. {@code keep} may be null. */
  void keepThatMusic$stopAllExcept(SoundInstance keep);
}
