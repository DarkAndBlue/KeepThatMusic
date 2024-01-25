package de.darkandblue.keepthatmusic.mixin;

import de.darkandblue.keepthatmusic.KeepThatMusic;
import de.darkandblue.keepthatmusic.interfaces.IMixinSoundManager;
import de.darkandblue.keepthatmusic.interfaces.IMixinSoundSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.Channel;
import net.minecraft.client.sound.MusicTracker;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundSystem;
import net.minecraft.sound.MusicSound;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(MusicTracker.class)
public class MixinMusicTracker {
	@Shadow
	private SoundInstance current;
	@Final
	@Shadow
	private MinecraftClient client;
	
	SoundSystem lastSoundSystem;
	Channel.SourceManager lastSourceManager;
	SoundInstance lastSoundInstace;
	
	/**
	 * When a respawn packet comes in (respawn packet is also used for dimension switching like nether portals)
	 * Forge-Mappings: MusicManager.stopPlaying()
	 */
	@Inject(method = "stop", at = @At("HEAD"), cancellable = true)
	public void stop(CallbackInfo ci) {
		if (KeepThatMusic.DEBUG) {
			System.out.println("MusicTracker.stop() " + Math.random());
		}
		
//		SoundSystem soundSystem = ((IMixinSoundManager) client.getSoundManager()).getSoundSystem();
//		Channel.SourceManager sourceManager = ((IMixinSoundSystem) soundSystem).sourceManagerBySoundInstance(current);
//		System.out.println(sourceManager.isStopped());
//		
//		lastSoundSystem = soundSystem;
//		lastSourceManager = sourceManager;
//		lastSoundInstace = current;
		
		// TODO: save music on stopping (also in other mixin functions) and play it again when starting
		
//		ci.cancel();
	}
	
	@Inject(method = "play", at = @At("HEAD"), cancellable = true)
	public void play(MusicSound type, CallbackInfo ci) {
		if (KeepThatMusic.DEBUG) {
			System.out.println("MusicTracker.play() " + Math.random());
		}
		if(lastSoundInstace != null) {
			current = lastSoundInstace;
			ci.cancel();
		}
	}

//	@Shadow
//	private int timeUntilNextSong;
//	@Inject(method = "tick", at = @At("HEAD"))
//	private void tick(CallbackInfo callbackInfo) {
//		System.out.println(timeUntilNextSong);
//	}
}
