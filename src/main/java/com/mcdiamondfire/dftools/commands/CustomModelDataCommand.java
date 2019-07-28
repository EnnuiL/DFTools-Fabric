package com.mcdiamondfire.dftools.commands;

import com.mcdiamondfire.dftools.utils.ItemUtils;
import com.mcdiamondfire.dftools.utils.MessageUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.context.CommandContext;
import io.github.cottonmc.clientcommands.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;

public class CustomModelDataCommand {
    private static final MinecraftClient minecraft = MinecraftClient.getInstance();

    public static void register(CommandDispatcher<CottonClientCommandSource> dispatcher) {
        dispatcher.register(ArgumentBuilders.literal("custommodeldata")
            .then(ArgumentBuilders.argument("id", IntegerArgumentType.integer())
                    .executes(ctx -> runCustomModel("set", ctx)))
            .executes(ctx -> runCustomModel("clear", ctx))
        );
    }

    private static int runCustomModel(String string, CommandContext<CottonClientCommandSource> context) throws CommandSyntaxException {
        if (!minecraft.player.isCreative()) {
			MessageUtils.errorMessage("You need to be in build mode or dev mode to do this!");
			return 0;
        }

        switch (string) {
            case "set":
                setCustomModel(context);
                 return 1;
            case "clear":
                clearCustomModel(context);
                return 1;
        }

        return 0;
    }

    private static int setCustomModel(CommandContext<CottonClientCommandSource> context) throws CommandSyntaxException {
        ItemStack itemStack = minecraft.player.getMainHandStack();
        
        Integer id = IntegerArgumentType.getInteger(context, "id");

        // Checks if item stack is not air.
        if (itemStack.isEmpty()) {
            MessageUtils.errorMessage("Invalid item!");
            return 0;
        }

        // Checks if item has an NBT tag.
        if (!itemStack.hasTag()) {
            itemStack.setTag(new CompoundTag());
        }
        
        itemStack.getTag().putInt("CustomModelData", id);
        //Sends updated item to the server.
        ItemUtils.setItemInHand(itemStack);
        
        MessageUtils.actionMessage("Added CustomModelData tag.");
        return 1;
    }
    private static int clearCustomModel(CommandContext<CottonClientCommandSource> context) throws CommandSyntaxException {
        ItemStack itemStack = minecraft.player.getMainHandStack();

        //Checks if item stack is not air.
		if (itemStack.isEmpty()) {
			MessageUtils.errorMessage("Invalid item!");
            return 0;
		}
        
		//Checks if item has an NBT tag.
		if (!itemStack.hasTag()) {
            MessageUtils.errorMessage("This item does not contain any CustomModelData tags!");
            return 0;
		}
        
		//Checks if item has a CustomModelData tag.
		if (!itemStack.getTag().containsKey("CustomModelData")) {
            MessageUtils.errorMessage("This item does not contain any CustomModelData tags!");
            return 0;
		}
        
        itemStack.getTag().remove("CustomModelData");

        //Deletes NBT tag if no tags remain.
        if (itemStack.getTag().getSize() == 0) {
            itemStack.setTag(null);
		}
		
        //Sends updated item to the server.
        ItemUtils.setItemInHand(itemStack);
		
        MessageUtils.actionMessage("Cleared all CustomModelData tags.");
        return 1;
    }
}
