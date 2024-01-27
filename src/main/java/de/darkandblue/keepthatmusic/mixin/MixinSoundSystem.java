package de.darkandblue.keepthatmusic.mixin;

import de.darkandblue.keepthatmusic.interfaces.IMixinSoundSystem;
import net.minecraft.client.sound.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(SoundSystem.class)
public abstract class MixinSoundSystem implements IMixinSoundSystem {
	@Final
	@Shadow
	private Map<SoundInstance, Channel.SourceManager> sources;
	
	public Channel.SourceManager sourceManagerBySoundInstance(SoundInstance soundInstance) {
		return sources.get(soundInstance);
	}
}
