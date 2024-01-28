package de.darkandblue.keepthatmusic;


import net.fabricmc.api.ClientModInitializer;

public class KeepThatMusic implements ClientModInitializer {
  public final static boolean DEBUG = false;
  public final static int MAX_MUSIC_DELAY = -1; // -1 = disabled, in ticks per second
  
  @Override
  public void onInitializeClient() {
  }
}