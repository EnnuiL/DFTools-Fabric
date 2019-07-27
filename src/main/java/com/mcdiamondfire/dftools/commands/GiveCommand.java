package com.mcdiamondfire.dftools.commands;

import com.mcdiamondfire.dftools.MessageUtils;
import com.mcdiamondfire.dftools.ItemUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.arguments.ItemStackArgumentType;

import io.github.cottonmc.clientcommands.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;

public class GiveCommand {
    private static final MinecraftClient minecraft = MinecraftClient.getInstance();

    public static void register(CommandDispatcher<CottonClientCommandSource> dispatcher) {
        dispatcher.register(ArgumentBuilders.literal("dfgive")
        .then(ArgumentBuilders.argument("item", ItemStackArgumentType.itemStack())
            .then(ArgumentBuilders.argument("count", IntegerArgumentType.integer())
                .executes(ctx -> execute(ctx, true)))
            .executes(ctx -> execute(ctx, false)))
        .executes(ctx -> {
                MessageUtils.errorMessage("Invalid item.");
                return 1;
        }));

        // Shortcut to /dfgive, same code as above.
        dispatcher.register(ArgumentBuilders.literal("dfg")
        .then(ArgumentBuilders.argument("item", ItemStackArgumentType.itemStack())
            .then(ArgumentBuilders.argument("count", IntegerArgumentType.integer())
                .executes(ctx -> execute(ctx, true)))
            .executes(ctx -> execute(ctx, false)))
        .executes(ctx -> {
                MessageUtils.errorMessage("Invalid item.");
                return 1;
        }));
    }

    private static int execute(CommandContext<CottonClientCommandSource> context, Boolean useAmount) throws CommandSyntaxException {
        Integer amount = 1;
        if (useAmount == true) {
            amount = IntegerArgumentType.getInteger(context, "count");
        } 

        ItemStack itemStack = ItemStackArgumentType.getItemStackArgument(context, "item").createStack(amount, false);

        //Checks if player is not in survival mode.
        if (!minecraft.player.isCreative()) {
            MessageUtils.errorMessage("You need to be in build mode or dev mode to do this!");
            return 1;
        }

        //Checks if item stack is not air.
		if (itemStack.isEmpty()) {
			MessageUtils.errorMessage("Invalid item!");
            return 1;
		}

        //Sends updated item to the server.
        ItemUtils.setItemInHotbar(itemStack, false);
        return 1;
    }
}