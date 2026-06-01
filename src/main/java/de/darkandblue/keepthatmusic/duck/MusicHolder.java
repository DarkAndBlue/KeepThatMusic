package de.darkandblue.keepthatmusic.duck;

import net.minecraft.client.resources.sounds.SoundInstance;

/**
 * Duck interface implemented (via mixin) on the game's MusicManager so other mixins can read the
 * currently playing music instance without depending on its private field directly.
 */
public interface MusicHolder {
  SoundInstance keepThatMusic$getCurrentMusic();
}
