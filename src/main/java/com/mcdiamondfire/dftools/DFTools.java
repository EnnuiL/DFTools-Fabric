package com.mcdiamondfire.dftools;

import com.mcdiamondfire.dftools.commands.GiveCommand;
import com.mcdiamondfire.dftools.screen.GiveCommandScreen;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.minecraft.client.MinecraftClient;
public class DFTools implements ClientModInitializer {
	private static final MinecraftClient minecraft = MinecraftClient.getInstance();

	@Override
	public void onInitializeClient() {
		ClientTickCallback.EVENT.register(e -> {
			if (GiveCommand.guiSummoned == true) {
				GiveCommand.guiSummoned = false;
				minecraft.openScreen(new GiveCommandScreen());
			}
		});
	}
}
