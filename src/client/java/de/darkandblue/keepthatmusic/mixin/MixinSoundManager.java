package de.darkandblue.keepthatmusic.mixin;

import de.darkandblue.keepthatmusic.KeepThatMusic;
import de.darkandblue.keepthatmusic.interfaces.IMixinSoundManager;
import de.darkandblue.keepthatmusic.interfaces.IMixinSoundSystem;
import net.minecraft.client.sound.Channel;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.sound.SoundSystem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SoundManager.class)
public class MixinSoundManager implements IMixinSoundManager {
	@Final
	@Shadow
	private SoundSystem soundSystem;
	
	/**
	 * When reconnecting the world (single player) it keeps playing the same music
	 * Forge-Mappings: SoundManager.destroy()
	 */
	@Inject(method = "close", at = @At("HEAD"), cancellable = true)
	private void close(CallbackInfo ci) {
		if (KeepThatMusic.DEBUG) {
			System.out.println("SoundManager.close() " + Math.random());
		}
		
		ci.cancel();
	}
	
	/**
	 * When pausing the game in single player it keeps playing the music
	 * Forge-Mappings: SoundManager.pause()
	 */
	@Inject(method = "pauseAll", at = @At("HEAD"), cancellable = true)
	private void pauseAll(CallbackInfo ci) {
		if (KeepThatMusic.DEBUG) {
			System.out.println("SoundManager.pauseAll() " + Math.random());
		}
		ci.cancel();
	}
	
	@Override
	public SoundSystem getSoundSystem() {
		return soundSystem;
	}
}
