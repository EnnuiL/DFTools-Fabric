package com.mcdiamondfire.dftools.commands;

import com.mcdiamondfire.dftools.utils.ItemUtils;
import com.mcdiamondfire.dftools.utils.MessageUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.context.CommandContext;

import io.github.cottonmc.clientcommands.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.nbt.CompoundTag;

public class RenameCommand {
    private static final MinecraftClient minecraft = MinecraftClient.getInstance();

    public static void register(CommandDispatcher<CottonClientCommandSource> dispatcher) {
        dispatcher.register(ArgumentBuilders.literal("rename")
                .then(ArgumentBuilders.argument("name", StringArgumentType.greedyString())
                    .executes(ctx -> runRename("rename", ctx)))
                .executes(ctx -> runRename("clear", ctx))
        );
    }
    
    private static int runRename(String string, CommandContext<CottonClientCommandSource> context) throws CommandSyntaxException {
        if (!minecraft.player.isCreative()) {
            MessageUtils.errorMessage("You need to be in build mode or dev mode to do this!");
            return 1;
        }

        switch (string) {
        case "rename":
            rename(context);
            return 1;
        case "clear":
            clearRename(context);
            return 1;
        }

        return 1;
    }

    private static int rename(CommandContext<CottonClientCommandSource> context) throws CommandSyntaxException {
        ItemStack itemStack = minecraft.player.getMainHandStack();

        // Checks if item stack is not air.
        if (itemStack.isEmpty()) {
            MessageUtils.errorMessage("Invalid item!");
            return 1;
        }

        // Checks if item has an NBT tag.
        if (!itemStack.hasTag()) {
            itemStack.setTag(new CompoundTag());
        }
        
        String name = StringArgumentType.getString(context, "name");
        itemStack.setCustomName(new LiteralText(name.replaceAll("&([0-9a-z]+)", "§$1")));
        //Sends updated item to the server.
        ItemUtils.setItemInHand(itemStack);
        return 1;
    }
    
    private static int clearRename(CommandContext<CottonClientCommandSource> context) throws CommandSyntaxException {
        ItemStack itemStack = minecraft.player.getMainHandStack();

        //Checks if item stack is not air.
		if (itemStack.isEmpty()) {
			MessageUtils.errorMessage("Invalid item!");
            return 1;
		}
        
		//Checks if item has an NBT tag.
		if (!itemStack.hasTag()) {
            MessageUtils.errorMessage("This item does not contain any tags!");
            return 1;
		}
		
		//Checks if item has a Display tag.
		if (!itemStack.getTag().containsKey("display")) {
            MessageUtils.errorMessage("This item does not contain any custom name!");
            return 1;
		}
        
        itemStack.getTag().remove("display");
        
        //Deletes NBT tag if no tags remain.
        if (itemStack.getTag().getSize() == 0) {
            itemStack.setTag(null);
		}
        
        //Sends updated item to the server.
        ItemUtils.setItemInHand(itemStack);
		
        MessageUtils.actionMessage("Cleared the custom name.");
        return 1;
    }
}
