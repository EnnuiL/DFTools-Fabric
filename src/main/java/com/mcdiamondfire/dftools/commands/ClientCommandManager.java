package com.mcdiamondfire.dftools.commands;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.ChatFormat;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.command.CommandException;
import net.minecraft.network.chat.*;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.HoverEvent;

import java.util.HashSet;
import java.util.Set;

public class ClientCommandManager {

    private static Set<String> clientSideCommands = new HashSet<>();

    public static void clearClientSideCommands() {
        clientSideCommands.clear();
    }

    public static void addClientSideCommand(String name) {
        clientSideCommands.add(name);
    }

    public static boolean isClientSideCommand(String name) {
        return clientSideCommands.contains(name);
    }

    public static void sendError(Component error) {
        sendFeedback(new TextComponent("").append(error).applyFormat(ChatFormat.RED).toString());
    }

    public static void sendFeedback(String message) {
        sendFeedback(new TranslatableComponent(message).toString());
    }

    public static void sendFeedback(TextComponent message) {
        MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(message);
    }

    public static int executeCommand(StringReader reader, String command) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        try {
            player.networkHandler.getCommandDispatcher().execute(reader, new FakeCommandSource(player));
        } catch (CommandException e) {
            ClientCommandManager.sendError(e.getMessageComponent());
            return 0;
        } catch (CommandSyntaxException e) {
            ClientCommandManager.sendError(Components.message(e.getRawMessage()));
            if (e.getInput() != null && e.getCursor() >= 0) {
                int cursor = Math.min(e.getCursor(), e.getInput().length());
                Component text = new TextComponent("").applyFormat(ChatFormat.GRAY)
                        .modifyStyle(style -> style.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command)));
                if (cursor > 10)
                    text.append("...");

                text.append(e.getInput().substring(Math.max(0, cursor - 10), cursor));
                if (cursor < e.getInput().length()) {
                    text.append(new TextComponent(e.getInput().substring(cursor)).applyFormat(ChatFormat.RED, ChatFormat.UNDERLINE));
                }

                text.append(new TranslatableComponent("command.context.here").applyFormat(ChatFormat.RED, ChatFormat.ITALIC));
                ClientCommandManager.sendError(text);
                return 0;
            }
        } catch (Exception e) {
            Component error = new TextComponent(e.getMessage() == null ? e.getClass().getName() : e.getMessage());
            ClientCommandManager.sendError(new TranslatableComponent("command.failed")
                .modifyStyle(style -> style.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, error))));
            e.printStackTrace();
            return 0;
        }
        return 1;
    }
}
