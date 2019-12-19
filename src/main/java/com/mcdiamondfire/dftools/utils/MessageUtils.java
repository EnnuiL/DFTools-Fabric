package com.mcdiamondfire.dftools.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;

public class MessageUtils {
	private static final MinecraftClient minecraft = MinecraftClient.getInstance();
	
	private static final String INFO_PREFIX = "§b❱§3❱ §b";
	private static final String WARN_PREFIX = "§6❱§c❱ §e";
	private static final String ERROR_PREFIX = "§c❱§4❱ §c";
	private static final String ACTION_PREFIX = "§d❱§5❱ §d";
	private static final String NOTE_PREFIX = "§6❱§b❱ §e";
	
	public static void infoMessage(String message) {
		minecraft.player.sendMessage(new LiteralText(INFO_PREFIX + message));
	}
	
	public static void infoMessage(String message, boolean playSound) {
		minecraft.player.sendMessage(new LiteralText(INFO_PREFIX + message));
		if (playSound) minecraft.player.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 1f, 0f);
	}
	
	public static void warnMessage(String message) {
		minecraft.player.sendMessage(new LiteralText(WARN_PREFIX + message));
	}
	
	public static void errorMessage(String message) {
		minecraft.player.sendMessage(new LiteralText(ERROR_PREFIX + message));
		minecraft.player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_DIDGERIDOO, 1f, 0f);
	}
	
	public static void actionMessage(String message) {
		minecraft.player.sendMessage(new LiteralText(ACTION_PREFIX + message));
	}
	
	public static void noteMessage(String message) {
		minecraft.player.sendMessage(new LiteralText(NOTE_PREFIX + message));
	}
}