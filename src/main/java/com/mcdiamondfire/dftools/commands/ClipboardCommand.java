package com.mcdiamondfire.dftools.commands;

import com.mcdiamondfire.dftools.utils.MessageUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.context.CommandContext;

import io.github.cottonmc.clientcommands.*;
import net.minecraft.client.MinecraftClient;

public class ClipboardCommand {
    private static final MinecraftClient minecraft = MinecraftClient.getInstance();

    public static void register(CommandDispatcher<CottonClientCommandSource> dispatcher) {
        dispatcher.register(ArgumentBuilders.literal("dftcopy")
                .then(ArgumentBuilders.literal("text")
                    .then(ArgumentBuilders.argument("textToCopy", StringArgumentType.greedyString())
                        .executes(ctx -> execute(ctx))))
                    .executes(ctx -> {
                        MessageUtils.errorMessage("No text was detected!");
                        return 1;
                    })
                .executes(ctx -> {
                    MessageUtils.errorMessage("Invalid mode!");
                    return 1;
                })
        );
    }

    private static int execute(CommandContext<CottonClientCommandSource> context) throws CommandSyntaxException {
        //Get the string.
        String textToCopy = StringArgumentType.getString(context, "textToCopy");

        //Copy the string to the clipboard.
        minecraft.keyboard.setClipboard(textToCopy);

        //Sends the message.
        MessageUtils.infoMessage("Text successfully copied to clipboard.");
        return 1;
    }
}
