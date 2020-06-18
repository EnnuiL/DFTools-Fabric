package com.mcdiamondfire.dftools;

import com.mcdiamondfire.dftools.commands.GiveCommand;
import com.mcdiamondfire.dftools.screen.CodeTemplateScreen;
import com.mcdiamondfire.dftools.screen.GiveCommandScreen;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
public class DFToolsMod implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ClientTickCallback.EVENT.register(client -> {
			if (GiveCommand.guiSummoned != 0) {
				//Summoned by /dfgive
				if (GiveCommand.guiSummoned == 1) {
					//This allows for holding the arrow key while in the custom GUI.
					client.keyboard.enableRepeatEvents(true);
					client.openScreen(new GiveCommandScreen());
				}

				//Summoned by /dfgive codetemplate
				if (GiveCommand.guiSummoned == 2) {
					client.keyboard.enableRepeatEvents(true);
					client.openScreen(new CodeTemplateScreen());
				}

				GiveCommand.guiSummoned = 0;
			}
		});
	}
}
