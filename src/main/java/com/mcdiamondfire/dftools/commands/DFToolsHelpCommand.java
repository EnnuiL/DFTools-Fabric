package com.mcdiamondfire.dftools.commands;

import com.mcdiamondfire.dftools.utils.MessageUtils;
import com.mojang.brigadier.CommandDispatcher;
import io.github.cottonmc.clientcommands.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;

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
            "§6> §e/breakable",
            "§6> §e/candestroy",
            "§6> §e/canplace",
            "§6> §e/custommodeldata",
            "§6> §e/dfgive   §7Works like /give, has a menu when typed alone.",
            "§6> §e/dfg get  §7Gives the held item's ID ready for /dfgive.",
            "§6> §e/dfg codetemplate §7Opens a menu for code templates.",
            "§6> §e/editname",
            "§6> §e/rename  §7Similar to the default /rename command.",
            "§6> §e/unbreakable §7Similar to the default /unbreakable command.",
			""
        };

		for (String messageLine : helpMessage) {
			minecraft.player.sendMessage(new LiteralText(messageLine));
		}
	}
}
