package com.mcdiamondfire.dftools;

import com.mcdiamondfire.dftools.commands.GiveCommand;
import com.mcdiamondfire.dftools.screen.CodeTemplateScreen;
import com.mcdiamondfire.dftools.screen.GiveCommandScreen;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.minecraft.client.MinecraftClient;
public class DFToolsMod implements ClientModInitializer {
	private static final MinecraftClient minecraft = MinecraftClient.getInstance();

	@Override
	public void onInitializeClient() {
		ClientTickCallback.EVENT.register(e -> {
			if (GiveCommand.guiSummoned != 0) {
				if (GiveCommand.guiSummoned == 1) {
					minecraft.openScreen(new GiveCommandScreen());
				}

				if (GiveCommand.guiSummoned == 2) {
					minecraft.openScreen(new CodeTemplateScreen());
				}

				GiveCommand.guiSummoned = 0;
			}
		});
	}
}
