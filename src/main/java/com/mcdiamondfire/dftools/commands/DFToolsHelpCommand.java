package com.mcdiamondfire.dftools.commands;

import com.mcdiamondfire.dftools.MessageUtils;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;
import io.github.cottonmc.clientcommands.*;

public class DFToolsHelpCommand {
    private static final MinecraftClient minecraft = MinecraftClient.getInstance();

    public static void register(CommandDispatcher<CottonClientCommandSource> dispatcher) {
        dispatcher.register(ArgumentBuilders.literal("dftools")
            .then(ArgumentBuilders.literal("commands")
                .executes(ctx -> {
                    showCommandHelp();
                    return 1;
                })
            )
            .executes(ctx -> {
                MessageUtils.errorMessage("Invalid help category.");
                return 1;
            })
        );
    }

    private static void showCommandHelp() {
		
		String[] helpMessage = new String[] {
			"§6§m    §6[§eCommands§6]§m    ",
			"",
			"  §cNote: §7Check out the command's autocomplete.",
			"",
			"§6> §e/candestroy",
			"§6> §e/canplace",
			"§6> §e/editname",
			"§6> §e/itemdata §7Displays the NBT for the currently held item.",
            //"§6> §e/rename  §7Similar to the default /rename command.",
			""
		};
		
		for (String messageLine : helpMessage) {
			minecraft.player.sendMessage(new LiteralText(messageLine));
		}
	}
}
