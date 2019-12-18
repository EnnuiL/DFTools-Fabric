package com.mcdiamondfire.dftools.commands;

import com.mcdiamondfire.dftools.utils.MessageUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.context.CommandContext;

import io.github.cottonmc.clientcommands.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;
import net.minecraft.text.Text;

public class ItemDataCommand {
    private static final MinecraftClient minecraft = MinecraftClient.getInstance();

    public static void register(CommandDispatcher<CottonClientCommandSource> dispatcher) {
        dispatcher.register(ArgumentBuilders.literal("itemdata")
            .executes(ctx -> execute(ctx)));
    }

    private static int execute(CommandContext<CottonClientCommandSource> context) throws CommandSyntaxException {
        ItemStack itemStack = minecraft.player.getMainHandStack();

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
		
		//Checks if item has an NBT tag.
		if (!itemStack.hasTag()) {
            MessageUtils.errorMessage("This item does not contain any tags!");
            return 1;
		}
        
        String itemTag = itemStack.getTag().toString().replaceAll("§", "&");
        
        //Creates the click and hover events for the message.
		Style messageStyle = new Style();
		messageStyle.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/dftools-copytoclipboard " + itemTag));
		messageStyle.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new LiteralText("Click here to copy the\nNBT to your clipboard.").formatted(Formatting.BLUE)));
		
		//Creates the actual message text component.
		Text messageText = new LiteralText(itemTag).formatted(Formatting.BLUE);
		messageText.setStyle(messageStyle);
		
		//Sends the message.
		MessageUtils.infoMessage("Item NBT:");
        minecraft.player.sendMessage(messageText);
        return 1;
    }
}
