package com.mcdiamondfire.dftools.mixin;

import com.mojang.brigadier.StringReader;
import com.mcdiamondfire.dftools.commands.ClientCommandManager;
import net.minecraft.ChatFormat;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.command.CommandException;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public abstract class PlayerMixin {
    @Shadow @Final protected MinecraftClient client;

    @Shadow public abstract void addChatMessage(Component text_1, boolean boolean_1);

    @Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
    private void onChatMessage(String msg, CallbackInfo info) {
        if (msg.length() < 2 || !msg.startsWith("/")) return;
        boolean cancel = false;
        StringReader reader = new StringReader(msg);
        int cursor = reader.getCursor();
        String commandName = reader.canRead() ? reader.readUnquotedString() : "";
        reader.setCursor(cursor);
        if (ClientCommandManager.isClientSideCommand(commandName)) {
            try {
                // The game freezes when using heavy commands. Run your heavy code somewhere else pls
                int result = ClientCommandManager.executeCommand(reader, msg);
                if (result != 1)
                    // Prevent sending the message
                    cancel = true;
            } catch (CommandException e) {
                addChatMessage(e.getMessageComponent().applyFormat(ChatFormat.RED), false);
                cancel = true;
            } catch (Exception e) {
                addChatMessage(new TranslatableComponent("command.failed").applyFormat(ChatFormat.RED), false);
                cancel = true;
            }
        }
        
        if (cancel)
            info.cancel();
    }
}
