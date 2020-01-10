package com.mcdiamondfire.dftools.commands;

import com.mcdiamondfire.dftools.utils.ItemUtils;
import com.mcdiamondfire.dftools.utils.MessageUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.context.CommandContext;
import io.github.cottonmc.clientcommands.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.arguments.BlockStateArgument;
import net.minecraft.command.arguments.BlockStateArgumentType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.registry.Registry;

public class CanDestroyCommand {
    private static final MinecraftClient minecraft = MinecraftClient.getInstance();

    public static void register(CommandDispatcher<CottonClientCommandSource> dispatcher) {
        dispatcher.register(ArgumentBuilders.literal("candestroy")
            .then(ArgumentBuilders.literal("add")
                .then(ArgumentBuilders.argument("id", BlockStateArgumentType.blockState())
                    .executes(ctx -> runCanDestroy("add", ctx)))
                .executes(ctx -> {
                    MessageUtils.errorMessage("Invalid block name.");
                    return 1;
                })
            )
            .then(ArgumentBuilders.literal("remove")
                .then(ArgumentBuilders.argument("id", BlockStateArgumentType.blockState())
                    .executes(ctx -> runCanDestroy("remove", ctx)))
                .executes(ctx -> {
                    MessageUtils.errorMessage("Invalid block name.");
                    return 1;
                })
            )
            .then(ArgumentBuilders.literal("clear")
                .executes(ctx -> runCanDestroy("clear", ctx))
            )
            .executes(ctx -> {
                MessageUtils.errorMessage("Invalid mode.");
                return 1;
            })
        );
    }

    private static int runCanDestroy(String string, CommandContext<CottonClientCommandSource> context) throws CommandSyntaxException {
        if (!minecraft.player.isCreative()) {
			MessageUtils.errorMessage("You need to be in build mode or dev mode to do this!");
			return 1;
        }

        switch (string) {
            case "add":
                addCanDestroy(context);
                 return 1;
            case "remove":
                removeCanDestroy(context);
                return 1;
            case "clear":
                clearCanDestroy(context);
                return 1;
        }

        return 1;
    }

    private static int addCanDestroy(CommandContext<CottonClientCommandSource> context) throws CommandSyntaxException {
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

        // Checks if item has a CanDestroy tag.
        if (!itemStack.getTag().contains("CanDestroy", 9)) {
            itemStack.getTag().put("CanDestroy", new ListTag());
        }

        BlockStateArgument tag = context.getArgument("id", BlockStateArgument.class);
        StringTag stringTag = StringTag.of(Registry.BLOCK.getId(tag.getBlockState().getBlock()).toString());

        itemStack.getTag().getList("CanDestroy", 8).add(stringTag);
        //Sends updated item to the server.
        ItemUtils.setItemInHand(itemStack);
        
        MessageUtils.actionMessage("Added CanDestroy tag.");
        return 1;
    }
    private static int removeCanDestroy(CommandContext<CottonClientCommandSource> context) throws CommandSyntaxException {
        ItemStack itemStack = minecraft.player.getMainHandStack();

        //Checks if item stack is not air.
		if (itemStack.isEmpty()) {
			MessageUtils.errorMessage("Invalid item!");
            return 1;
		}
		
		//Checks if item has an NBT tag.
		if (!itemStack.hasTag()) {
            MessageUtils.errorMessage("This item does not contain any CanDestroy tags!");
            return 1;
		}
		
		//Checks if item has a CanDestroy tag.
		if (!itemStack.getTag().contains("CanDestroy", 9)) {
            MessageUtils.errorMessage("This item does not contain any CanDestroy tags!");
            return 1;
        }
        
        if (itemStack.getTag().getList("CanDestroy", 8).size() == 0) {
            MessageUtils.errorMessage("This item does not contain any CanDestroy tags!");
            return 1;
		}
        
        ListTag listTag = itemStack.getTag().getList("CanDestroy", 8);
        BlockStateArgument tag = context.getArgument("id", BlockStateArgument.class);
        for (int i = 0; i < listTag.size(); i++) {
            System.out.println(listTag.get(i).toString());
            System.out.println(tag.toString());
            //Checks if CanDestroy tag is the specified block, if so, removes it.
			if (listTag.getString(i).equalsIgnoreCase(Registry.BLOCK.getId(tag.getBlockState().getBlock()).toString())) {
                itemStack.getTag().getList("CanDestroy", 8).remove(i);
                
                if (itemStack.getTag().getList("CanDestroy", 8).size() == 0) {
                    itemStack.getTag().remove("CanDestroy");
                }

                //Sends updated item to the server.
                ItemUtils.setItemInHand(itemStack);
                
				MessageUtils.actionMessage("Removed CanDestroy tag.");
				return 1;
			}
		}

        MessageUtils.errorMessage("Could not find specified CanDestroy tag.");
        return 1;
    }
    private static int clearCanDestroy(CommandContext<CottonClientCommandSource> context) throws CommandSyntaxException {
        ItemStack itemStack = minecraft.player.getMainHandStack();

        //Checks if item stack is not air.
		if (itemStack.isEmpty()) {
			MessageUtils.errorMessage("Invalid item!");
            return 1;
		}
        
		//Checks if item has an NBT tag.
		if (!itemStack.hasTag()) {
            MessageUtils.errorMessage("This item does not contain any CanDestroy tags!");
            return 1;
		}
        
		//Checks if item has a CanDestroy tag.
        if (!itemStack.getTag().contains("CanDestroy", 9)) {
            MessageUtils.errorMessage("This item does not contain any CanDestroy tags!");
            return 1;
		}
        
        itemStack.getTag().remove("CanDestroy");

        //Deletes NBT tag if no tags remain.
        if (itemStack.getTag().getSize() == 0) {
            itemStack.setTag(null);
		}
		
        //Sends updated item to the server.
        ItemUtils.setItemInHand(itemStack);
		
        MessageUtils.actionMessage("Cleared all CanDestroy tags.");
        return 1;
    }
}
