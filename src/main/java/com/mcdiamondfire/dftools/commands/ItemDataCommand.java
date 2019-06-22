package com.mcdiamondfire.dftools.commands;

import com.mcdiamondfire.dftools.commands.ClientCommandManager;
import com.mcdiamondfire.dftools.MessageUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.ChatFormat;
import net.minecraft.network.chat.Component;
import net.minecraft.server.command.ServerCommandSource;

import static net.minecraft.server.command.CommandManager.*;

public class ItemDataCommand {
    private static final MinecraftClient minecraft = MinecraftClient.getInstance();

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        ClientCommandManager.addClientSideCommand("itemdata");

        dispatcher.register(literal("itemdata")
            .executes(ctx -> execute(ctx)));
    }

    private static int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ItemStack itemStack = minecraft.player.getMainHandStack();

        //Checks if player is not in survival mode.
        if (!minecraft.player.isCreative()) {
            MessageUtils.errorMessage("You need to be in build mode or dev mode to do this!");
            return 0;
        }

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
        
        String itemTag = itemStack.getTag().toString().replaceAll("§", "&");

        //Creates the click and hover events for the message.
		Style messageStyle = new Style();
		messageStyle.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, itemTag));
		messageStyle.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent("Click here to write the\nNBT to your chat bar.").applyFormat(ChatFormat.BLUE)));
		
		//Creates the actual message text component.
		Component messageComponent = new TextComponent(itemTag).applyFormat(ChatFormat.BLUE);
		messageComponent.setStyle(messageStyle);
		
		//Sends the message.
		MessageUtils.infoMessage("Item NBT:");
		minecraft.player.sendMessage(messageComponent);
        return 1;
    }
}
