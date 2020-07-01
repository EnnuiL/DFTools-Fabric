package com.mcdiamondfire.dftools;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.mcdiamondfire.dftools.commands.GiveCommand;
import com.mcdiamondfire.dftools.screen.CodeTemplateScreen;
import com.mcdiamondfire.dftools.screen.GiveCommandScreen;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class DFToolsMod implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ClientTickEvents.START_CLIENT_TICK.register(client -> {
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
