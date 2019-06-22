package com.mcdiamondfire.dftools;

import com.mcdiamondfire.dftools.commands.*;
import com.mojang.brigadier.CommandDispatcher;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.registry.CommandRegistry;
import net.minecraft.server.command.ServerCommandSource;

public class DFTools implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		CommandRegistry.INSTANCE.register(false, CanDestroyCommand::register);
		CommandRegistry.INSTANCE.register(false, CanPlaceCommand::register);
		CommandRegistry.INSTANCE.register(false, DFToolsHelpCommand::register);
		CommandRegistry.INSTANCE.register(false, EditNameCommand::register);
		CommandRegistry.INSTANCE.register(false, ItemDataCommand::register);
		CommandRegistry.INSTANCE.register(false, RenameCommand::register);
	}
	
	public static void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher) {
		CanDestroyCommand.register(dispatcher);
		CanPlaceCommand.register(dispatcher);
		DFToolsHelpCommand.register(dispatcher);
		EditNameCommand.register(dispatcher);
		ItemDataCommand.register(dispatcher);
		RenameCommand.register(dispatcher);
    }
}
