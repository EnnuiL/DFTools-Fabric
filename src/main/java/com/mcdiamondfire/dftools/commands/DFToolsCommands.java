package com.mcdiamondfire.dftools.commands;

import com.mojang.brigadier.CommandDispatcher;
import io.github.cottonmc.clientcommands.*;

public class DFToolsCommands implements ClientCommandPlugin {
    @Override
    public void registerCommands(CommandDispatcher<CottonClientCommandSource> dispatcher) {
        BreakableCommand.register(dispatcher);
        CanDestroyCommand.register(dispatcher);
        CanPlaceCommand.register(dispatcher);
        CustomModelDataCommand.register(dispatcher);
        DFToolsHelpCommand.register(dispatcher);
        EditNameCommand.register(dispatcher);
        GiveCommand.register(dispatcher);
        ItemDataCommand.register(dispatcher);
        RenameCommand.register(dispatcher);
        UnbreakableCommand.register(dispatcher);
    }
}