package com.mcdiamondfire.dftools.commands;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mcdiamondfire.dftools.utils.MessageUtils;
import com.mcdiamondfire.dftools.utils.ParserUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import com.mojang.brigadier.context.CommandContext;

import io.github.cottonmc.clientcommands.*;
import net.minecraft.client.MinecraftClient;

public class TestCommand {
    private static final MinecraftClient minecraft = MinecraftClient.getInstance();
    
    public static void register(CommandDispatcher<CottonClientCommandSource> dispatcher) {
        dispatcher.register(ArgumentBuilders.literal("dftest")
            .executes(ctx -> execute(ctx)));
    }

    private static int execute(CommandContext<CottonClientCommandSource> context) throws CommandSyntaxException {
        String[] code = new String[] {
            "event command",
            "	player.sendMessage:all(\"hey ya\")"
        };

        JsonObject JSON = new JsonObject();
        JSON.add("blocks", new JsonArray());

        //Checks if player is not in survival mode.
        if (!minecraft.player.isCreative()) {
            MessageUtils.errorMessage("You need to be in build mode or dev mode to do this!");
            return 1;
        }

        Integer lookForTabs = 0;

        for (int i = 0; i < code.length; i++) {
            if (code[i].startsWith("event")) {
                System.out.println("Event detected!");
                JsonObject jsonEvent = ParserUtils.parseEvent(code[i].replace("event ", ""));
                JSON.get("blocks").getAsJsonArray().add(jsonEvent);
                MessageUtils.infoMessage(jsonEvent.toString());
                lookForTabs += 1;
            } else if (lookForTabs != 0) {
                System.out.println(code[i]);
                if (code[i].strip() != code[i]) {
                    code[i] = code[i].strip();
                    JsonObject jsonAction = ParserUtils.parseAction(code[i]);
                    JSON.get("blocks").getAsJsonArray().add(jsonAction);
                    MessageUtils.infoMessage(JSON.toString());
                } else {
                    lookForTabs -= 1;
                }
            }
        }
        return 1;
    }
}
