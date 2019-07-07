package com.mcdiamondfire.dftools.commands;

import com.mcdiamondfire.dftools.MessageUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.arguments.MessageArgumentType;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.LiteralText;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Hand;

import static net.minecraft.server.command.CommandManager.*;

public class RenameCommand {
    private static final MinecraftClient minecraft = MinecraftClient.getInstance();

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("rename")
                .then(argument("name", MessageArgumentType.message()).executes(ctx -> runRename("rename", ctx)))
                .executes(ctx -> runRename("clear", ctx)));
    }

    private static int runRename(String string, CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        if (!minecraft.player.isCreative()) {
            MessageUtils.errorMessage("You need to be in build mode or dev mode to do this!");
            return 0;
        }

        switch (string) {
        case "rename":
            rename(context);
            return 1;
        case "clear":
            clearRename(context);
            return 1;
        }

        return 0;
    }

    private static int rename(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ItemStack itemStack = minecraft.player.getMainHandStack();

        // Checks if item stack is not air.
        if (itemStack.isEmpty()) {
            MessageUtils.errorMessage("Invalid item!");
            return 0;
        }

        // Checks if item has an NBT tag.
        if (!itemStack.hasTag()) {
            itemStack.setTag(new CompoundTag());
        }

        Text name = MessageArgumentType.getMessage(context, "name");
        itemStack.setCustomName(new LiteralText(name.asFormattedString().replaceAll("&([0-9a-z]+)", "§$1")));
        //Sends updated item to the server.
        minecraft.player.setStackInHand(Hand.MAIN_HAND, itemStack);
        return 1;
    }

    private static int clearRename(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ItemStack itemStack = minecraft.player.getMainHandStack();

        //Checks if item stack is not air.
		if (itemStack.isEmpty()) {
			MessageUtils.errorMessage("Invalid item!");
            return 0;
		}
		
		//Checks if item has an NBT tag.
		if (!itemStack.hasTag()) {
            MessageUtils.errorMessage("This item does not contain any tags!");
            return 0;
		}
		
		//Checks if item has a Display tag.
		if (!itemStack.getTag().containsKey("display")) {
            MessageUtils.errorMessage("This item does not contain any custom name!");
            return 0;
		}
        
        itemStack.getTag().remove("display");
        
        //Deletes NBT tag if no tags remain.
        if (itemStack.getTag().getSize() == 0) {
            itemStack.setTag(null);
		}
        
        //Sends updated item to the server.
        minecraft.player.setStackInHand(Hand.MAIN_HAND, itemStack);
		
        MessageUtils.actionMessage("Cleared the custom name.");
        return 1;
    }
}
