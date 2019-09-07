package com.mcdiamondfire.dftools.commands;

import com.mcdiamondfire.dftools.utils.ItemUtils;
import com.mcdiamondfire.dftools.utils.MathUtils;
import com.mcdiamondfire.dftools.utils.MessageUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import io.github.cottonmc.clientcommands.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;

public class LocationCommand {
    private static final MinecraftClient minecraft = MinecraftClient.getInstance();
    
    public static void register(CommandDispatcher<CottonClientCommandSource> dispatcher) {
        dispatcher.register(ArgumentBuilders.literal("location")
            .then(ArgumentBuilders.literal("move")
                .then(ArgumentBuilders.argument("x", DoubleArgumentType.doubleArg())
                    .then(ArgumentBuilders.argument("y", DoubleArgumentType.doubleArg())
                        .then(ArgumentBuilders.argument("z", DoubleArgumentType.doubleArg())
                            .then(ArgumentBuilders.argument("pitch", DoubleArgumentType.doubleArg())
                                .then(ArgumentBuilders.argument("yaw", DoubleArgumentType.doubleArg())
                                    .executes(ctx -> executeMove(ctx, 5))
                                )
                                .executes(ctx -> executeMove(ctx, 4))
                            )
                            .executes(ctx -> executeMove(ctx, 3))
                        )
                        .executes(ctx -> executeMove(ctx, 2))
                    )
                    .executes(ctx -> executeMove(ctx, 1))
                )
                .executes(ctx -> {
                    MessageUtils.errorMessage("No coordinates detected!");
                    return 1;
                })
            )
        );
    }
    
    private static int executeMove(CommandContext<CottonClientCommandSource> context, Integer paramCount) throws CommandSyntaxException {
        //Checks if player is not in survival mode.
        if (!minecraft.player.isCreative()) {
			MessageUtils.errorMessage("You need to be in build mode or dev mode to do this!");
			return 1;
        }
        
        ItemStack itemStack = minecraft.player.getMainHandStack();
        
        // Checks if item stack is not air.
        if (itemStack.isEmpty()) {
            MessageUtils.errorMessage("Invalid item!");
            return 1;
        }
        
        // Checks if item has an NBT tag.
        if (!itemStack.hasTag()) {
            MessageUtils.errorMessage("Invalid item!");
            return 1;
        }
        
        // Checks if item has the correct name.
        if (!itemStack.getName().getString().equals("§aLocation")) {
            MessageUtils.errorMessage("Invalid item!");
            return 1;
        }
        
        // Checks if item has HideFlags and if it's the correct one.
        if (!itemStack.getTag().containsKey("HideFlags") || !(itemStack.getTag().getInt("HideFlags") == 63)) {
			MessageUtils.errorMessage("Invalid item!");
			return 1;
		}
        
        // TODO: Remove the ViaVersion hack when DF gets updated to 1.14.4.
        ListTag itemLore = itemStack.getSubTag("display").getList("Lore", 8);
        
        // Dramatically shrinks the lines
        String loreLineBeginning = "{\"extra\":[{\"color\":\"gray\",\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"text\":\"";
        String loreLineEnd = ": \"},{\"color\":\"white\",\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"text\":\"";
        
        // Sets the X coordinate if specified.
        if (paramCount >= 1) {
            // Gets the parameter.
            double shiftX = DoubleArgumentType.getDouble(context, "x");
            // Extracts the coordinates from the location's lore.
            double originalX = Double.parseDouble(itemLore.getString(0).toString().replace(loreLineBeginning + "X" + loreLineEnd, "").replace("\"}],\"text\":\"\"}", ""));
            // Set the location coordinates.
            itemLore.setTag(0, new StringTag(loreLineBeginning + "X" + loreLineEnd + (MathUtils.round(originalX + shiftX, 2)) + "\"}],\"text\":\"\"}"));
        }
        
        // Sets the Y coordinate if specified.
        if (paramCount >= 2) {
            double shiftY = DoubleArgumentType.getDouble(context, "y");
            double originalY = Double.parseDouble(itemLore.getString(1).toString().replace(loreLineBeginning + "Y" + loreLineEnd, "").replace("\"}],\"text\":\"\"}", ""));
            itemLore.setTag(1, new StringTag(loreLineBeginning + "Y" + loreLineEnd + (MathUtils.round(originalY + shiftY, 2)) + "\"}],\"text\":\"\"}"));
        }
        
        // Sets the Z coordinate if specified. 
        if (paramCount >= 3) {
            double shiftZ = DoubleArgumentType.getDouble(context, "z");
            double originalZ = Double.parseDouble(itemLore.getString(2).toString().replace(loreLineBeginning + "Z" + loreLineEnd, "").replace("\"}],\"text\":\"\"}", ""));
            itemLore.setTag(2, new StringTag(loreLineBeginning + "Z" + loreLineEnd + (MathUtils.round(originalZ + shiftZ, 2)) + "\"}],\"text\":\"\"}"));
        }
        
        // Sets the pitch if specified.
        if (paramCount >= 4)  {
            double shiftPitch = DoubleArgumentType.getDouble(context, "pitch");
            double originalPitch = 0;
            originalPitch = Double.parseDouble(itemLore.getString(3).toString().replace(loreLineBeginning + "p" + loreLineEnd, "").replace("\"}],\"text\":\"\"}", ""));
            itemLore.setTag(3, new StringTag(loreLineBeginning + "p" + loreLineEnd + (MathUtils.roundAndClamp((originalPitch + shiftPitch), -90, 90, 2)) + "\"}],\"text\":\"\"}"));
        }
        
        // Sets the yaw if specified.
        if (paramCount >= 5) {
            double shiftYaw = DoubleArgumentType.getDouble(context, "yaw");
            double originalYaw = 0;
            originalYaw = Double.parseDouble(itemLore.getString(4).toString().replace(loreLineBeginning + "y" + loreLineEnd, "").replace("\"}],\"text\":\"\"}", ""));
            itemLore.setTag(4, new StringTag(loreLineBeginning + "y" + loreLineEnd + (MathUtils.roundAndClamp((originalYaw + shiftYaw), -180, 180, 2)) + "\"}],\"text\":\"\"}"));
        }
        
        //Sends updated item to the server.
        if (paramCount >= 1) {
            ItemUtils.setItemInHand(itemStack);
            MessageUtils.infoMessage("yay!");
        }

        return 1;
    }
}