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

public class CanDestroyCommand {
    private static final MinecraftClient minecraft = MinecraftClient.getInstance();

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        ClientCommandManager.addClientSideCommand("candestroy");

        dispatcher.register(literal("candestroy")
            .then(literal("add")
                .then(argument("id", BlockStateArgumentType.create())
                    .executes(ctx -> runCanDestroy("add", ctx)))
                .executes(ctx -> {
                    MessageUtils.errorMessage("Invalid block name.");
                     return 0;
                })
            )
            .then(literal("remove")
                .then(argument("id", BlockStateArgumentType.create())
                    .executes(ctx -> runCanDestroy("remove", ctx)))
                .executes(ctx -> {
                    MessageUtils.errorMessage("Invalid block name.");
                     return 0;
                })
            )
            .then(literal("clear")
                .executes(ctx -> runCanDestroy("clear", ctx))
            )
            .executes(ctx -> {
                MessageUtils.errorMessage("Invalid mode.");
                return 0;
            })
        );
    }

    private static int runCanDestroy(String string, CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        if (!minecraft.player.isCreative()) {
			MessageUtils.errorMessage("You need to be in build mode or dev mode to do this!");
			return 0;
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

        return 0;
    }

    private static int addCanDestroy(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
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
		
		//Checks if item has a CanDestroy tag.
		if (!itemStack.getTag().containsKey("CanDestroy", 9)) {
            itemStack.getTag().put("CanDestroy", new ListTag());
		}
        
        BlockStateArgument tag = BlockStateArgumentType.getBlockState(context, "id");
        itemStack.getTag().getList("CanDestroy", 8).add(new StringTag(Registry.BLOCK.getId(tag.getBlockState().getBlock()).toString()));
        //Sends updated item to the server.
        minecraft.player.setStackInHand(Hand.MAIN_HAND, itemStack);
		
        MessageUtils.actionMessage("Added CanDestroy tag.");
        return 1;
    }
    private static int removeCanDestroy(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ItemStack itemStack = minecraft.player.getMainHandStack();

        //Checks if item stack is not air.
		if (itemStack.isEmpty()) {
			MessageUtils.errorMessage("Invalid item!");
            return 0;
		}
		
		//Checks if item has an NBT tag.
		if (!itemStack.hasTag()) {
            MessageUtils.errorMessage("This item does not contain any CanDestroy tags!");
            return 0;
		}
		
		//Checks if item has a CanDestroy tag.
		if (!itemStack.getTag().containsKey("CanDestroy", 9)) {
            MessageUtils.errorMessage("This item does not contain any CanDestroy tags!");
            return 0;
        }
        
        if (itemStack.getTag().getList("CanDestroy", 8).size() == 0) {
            MessageUtils.errorMessage("This item does not contain any CanDestroy tags!");
            return 0;
		}
        
        ListTag listTag = itemStack.getTag().getList("CanDestroy", 8);
        BlockStateArgument tag = BlockStateArgumentType.getBlockState(context, "id");
        for (int i = 0; i < listTag.size(); i++) {
            System.out.println(listTag.get(i).toString());
            System.out.println(tag.toString());
            //Checks if CanDestroy tag is the specified block, if so, removes it.
			if (listTag.getString(i).equalsIgnoreCase(Registry.BLOCK.getId(tag.getBlockState().getBlock()).toString())) {
				itemStack.getTag().getList("CanDestroy", 8).remove(i);
                //Sends updated item to the server.
                if (itemStack.getTag().getList("CanDestroy", 8).size() == 0) {
                    itemStack.getTag().remove("CanDestroy");
                }
                minecraft.player.setStackInHand(Hand.MAIN_HAND, itemStack);
                
				MessageUtils.actionMessage("Removed CanDestroy tag.");
				return 1;
			}
		}

        MessageUtils.errorMessage("Could not find specified CanDestroy tag.");
        return 0;
    }
    private static int clearCanDestroy(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ItemStack itemStack = minecraft.player.getMainHandStack();

        //Checks if item stack is not air.
		if (itemStack.isEmpty()) {
			MessageUtils.errorMessage("Invalid item!");
            return 0;
		}
        
		//Checks if item has an NBT tag.
		if (!itemStack.hasTag()) {
            MessageUtils.errorMessage("This item does not contain any CanDestroy tags!");
            return 0;
		}
        
		//Checks if item has a CanDestroy tag.
		if (!itemStack.getTag().containsKey("CanDestroy", 9)) {
            MessageUtils.errorMessage("This item does not contain any CanDestroy tags!");
            return 0;
		}
        
        itemStack.getTag().remove("CanDestroy");

        //Deletes NBT tag if no tags remain.
        if (itemStack.getTag().getSize() == 0) {
            itemStack.setTag(null);
		}
		
        //Sends updated item to the server.
        minecraft.player.setStackInHand(Hand.MAIN_HAND, itemStack);
		
        MessageUtils.actionMessage("Cleared all CanDestroy tags.");
        return 1;
    }
}
