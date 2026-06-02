package de.darkandblue.keepthatmusic.duck;

import net.minecraft.client.resources.sounds.SoundInstance;

/**
 * Duck interface implemented (via mixin) on the game's MusicManager so other mixins can read the
 * currently playing music instance without depending on its private field directly.
 */
public interface MusicHolder {
  SoundInstance keepThatMusic$getCurrentMusic();

  /** Clears/sets the current music so the manager starts fresh music on its next tick. */
  void keepThatMusic$setCurrentMusic(SoundInstance music);
}
