package de.darkandblue.keepthatmusic.mixin;

import de.darkandblue.keepthatmusic.KeepThatMusic;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.commands.CommandSourceStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * CI-only smoke test driver. When launched with {@code -Dkeepthatmusic.test=true} (set by the
 * headless CI run), this waits until a single-player world is loaded and then runs a {@code
 * /playsound} command once via the integrated server's op-level command source. If playing a sound
 * in the modded game crashes, the client dies and the CI job fails. It is a no-op in normal play.
 */
@Mixin(Minecraft.class)
public abstract class MixinTestDriver {
  @Shadow
  public ClientLevel level;

  @Shadow
  public abstract IntegratedServer getSingleplayerServer();

  @Unique
  private boolean keepThatMusic$testDone;

  @Unique
  private int keepThatMusic$testTicks;

  @Inject(method = "tick", at = @At("RETURN"))
  private void keepThatMusic$testTick(CallbackInfo ci) {
    if (keepThatMusic$testDone || !Boolean.getBoolean("keepthatmusic.test")) {
      return;
    }
    IntegratedServer server = getSingleplayerServer();
    if (server == null || level == null) {
      return; // not in a single-player world yet
    }
    if (keepThatMusic$testTicks++ < 20) {
      return; // give the world ~1s to settle and the player to spawn
    }
    keepThatMusic$testDone = true;

    String command = "playsound minecraft:entity.experience_orb.pickup master @a";
    KeepThatMusic.LOGGER.info("[KeepThatMusic] CI test: executing /{}", command);
    server.execute(() -> {
      CommandSourceStack source = server.createCommandSourceStack();
      //? if >=1.19 {
      server.getCommands().performPrefixedCommand(source, command);
      //?} else {
      /*server.getCommands().performCommand(source, command);*/
      //?}
    });
  }
}
