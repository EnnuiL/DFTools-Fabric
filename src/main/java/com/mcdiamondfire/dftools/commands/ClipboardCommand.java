package com.mcdiamondfire.dftools.commands;

import com.mcdiamondfire.dftools.utils.MessageUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.context.CommandContext;

import io.github.cottonmc.clientcommands.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Clipboard;

public class ClipboardCommand {
    private static final MinecraftClient minecraft = MinecraftClient.getInstance();

    //TODO - Convert to "/clipboard copy" to be less messy.
    public static void register(CommandDispatcher<CottonClientCommandSource> dispatcher) {
        dispatcher.register(ArgumentBuilders.literal("dftools-copytoclipboard")
                .then(ArgumentBuilders.argument("textToCopy", StringArgumentType.greedyString())
                    .executes(ctx -> execute(ctx)))
                .executes(ctx -> {
                    MessageUtils.errorMessage("No text was detected!");
                    return 1;
                })
        );
    }

    private static int execute(CommandContext<CottonClientCommandSource> context) throws CommandSyntaxException {
        //Get the string.
        String textToCopy = StringArgumentType.getString(context, "textToCopy");

        //Creates a clipboard instance then copies the data.
        Clipboard clipboard = new Clipboard();
        clipboard.setClipboard(minecraft.window.getHandle(), textToCopy);

        //Sends the message.
        MessageUtils.infoMessage("Text successfully copied to clipboard.");
        return 1;
    }
}
