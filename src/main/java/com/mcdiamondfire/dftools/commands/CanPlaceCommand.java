package com.mcdiamondfire.dftools.commands;

import com.mcdiamondfire.dftools.commands.ClientCommandManager;
import com.mcdiamondfire.dftools.MessageUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.arguments.BlockStateArgument;
import net.minecraft.command.arguments.BlockStateArgumentType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Hand;
import net.minecraft.util.registry.Registry;

import static net.minecraft.server.command.CommandManager.*;

public class CanPlaceCommand {
    private static final MinecraftClient minecraft = MinecraftClient.getInstance();

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        ClientCommandManager.addClientSideCommand("canplace");

        dispatcher.register(literal("canplace")
            .then(literal("add")
                .then(argument("id", BlockStateArgumentType.create())
                    .executes(ctx -> runCanPlace("add", ctx)))
                .executes(ctx -> {
                    MessageUtils.errorMessage("Invalid block name.");
                     return 0;
                })
            )
            .then(literal("remove")
                .then(argument("id", BlockStateArgumentType.create())
                    .executes(ctx -> runCanPlace("remove", ctx)))
                .executes(ctx -> {
                    MessageUtils.errorMessage("Invalid block name.");
                     return 0;
                })
            )
            .then(literal("clear")
                .executes(ctx -> runCanPlace("clear", ctx))
            )
            .executes(ctx -> {
                MessageUtils.errorMessage("Invalid mode.");
                return 0;
            })
        );
    }
    
    private static int runCanPlace(String string, CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        if (!minecraft.player.isCreative()) {
			MessageUtils.errorMessage("You need to be in build mode or dev mode to do this!");
			return 0;
        }

        switch (string) {
            case "add":
                addCanPlace(context);
                 return 1;
            case "remove":
                removeCanPlace(context);
                return 1;
            case "clear":
                clearCanPlace(context);
                return 1;
        }

        return 0;
    }

    private static int addCanPlace(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ItemStack itemStack = minecraft.player.getMainHandStack();

        //Checks if item stack is not air.
		if (itemStack.isEmpty()) {
            MessageUtils.errorMessage("Invalid item!");
            return 0;
		}
        
		//Checks if item has an NBT tag.
		if (!itemStack.hasTag()) {
            itemStack.setTag(new CompoundTag());
		}
		
		//Checks if item has a CanPlaceOn tag.
		if (!itemStack.getTag().containsKey("CanPlaceOn", 9)) {
            itemStack.getTag().put("CanPlaceOn", new ListTag());
		}
        
        BlockStateArgument tag = BlockStateArgumentType.getBlockState(context, "id");
        itemStack.getTag().getList("CanPlaceOn", 8).add(new StringTag(Registry.BLOCK.getId(tag.getBlockState().getBlock()).toString()));
        //Sends updated item to the server.
        minecraft.player.setStackInHand(Hand.MAIN_HAND, itemStack);
        minecraft.player.swingHand(Hand.MAIN_HAND);
		
        MessageUtils.actionMessage("Added CanPlaceOn tag.");
        return 1;
    }

    private static int removeCanPlace(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ItemStack itemStack = minecraft.player.getMainHandStack();

        //Checks if item stack is not air.
		if (itemStack.isEmpty()) {
			MessageUtils.errorMessage("Invalid item!");
            return 0;
		}
		
		//Checks if item has an NBT tag.
		if (!itemStack.hasTag()) {
            MessageUtils.errorMessage("This item does not contain any CanPlaceOn tags!");
            return 0;
		}
		
		//Checks if item has a CanPlaceOn tag.
		if (!itemStack.getTag().containsKey("CanPlaceOn", 9)) {
            MessageUtils.errorMessage("This item does not contain any CanPlaceOn tags!");
            return 0;
        }
        
        if (itemStack.getTag().getList("CanPlaceOn", 8).size() == 0) {
            MessageUtils.errorMessage("This item does not contain any CanPlaceOn tags!");
            return 0;
		}
        
        ListTag listTag = itemStack.getTag().getList("CanPlaceOn", 8);
        BlockStateArgument tag = BlockStateArgumentType.getBlockState(context, "id");
        for (int i = 0; i < listTag.size(); i++) {
            System.out.println(listTag.get(i).toString());
            System.out.println(tag.toString());
            //Checks if CanPlaceOn tag is the specified block, if so, removes it.
			if (listTag.getString(i).equalsIgnoreCase(Registry.BLOCK.getId(tag.getBlockState().getBlock()).toString())) {
				itemStack.getTag().getList("CanPlaceOn", 8).remove(i);
                //Sends updated item to the server.
                if (itemStack.getTag().getList("CanPlaceOn", 8).size() == 0) {
                    itemStack.getTag().remove("CanPlaceOn");
                }
                minecraft.player.setStackInHand(Hand.MAIN_HAND, itemStack);
                minecraft.player.swingHand(Hand.MAIN_HAND);
                
				MessageUtils.actionMessage("Removed CanPlaceOn tag.");
				return 1;
			}
		}

        MessageUtils.errorMessage("Could not find specified CanPlaceOn tag.");
        return 0;
    }
    private static int clearCanPlace(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ItemStack itemStack = minecraft.player.getMainHandStack();

        //Checks if item stack is not air.
		if (itemStack.isEmpty()) {
			MessageUtils.errorMessage("Invalid item!");
            return 0;
		}
        
		//Checks if item has an NBT tag.
		if (!itemStack.hasTag()) {
            MessageUtils.errorMessage("This item does not contain any CanPlaceOn tags!");
            return 0;
		}
		
		//Checks if item has a CanPlaceOn tag.
		if (!itemStack.getTag().containsKey("CanPlaceOn", 9)) {
            MessageUtils.errorMessage("This item does not contain any CanPlaceOn tags!");
            return 0;
		}
        
        itemStack.getTag().remove("CanPlaceOn");
        
        //Deletes NBT tag if no tags remain.
        if (itemStack.getTag().getSize() == 0) {
            itemStack.setTag(null);
		}
        
        //Sends updated item to the server.
        minecraft.player.setStackInHand(Hand.MAIN_HAND, itemStack);
        minecraft.player.swingHand(Hand.MAIN_HAND);
		
        MessageUtils.actionMessage("Cleared all CanPlaceOn tags.");
        return 1;
    }
}
