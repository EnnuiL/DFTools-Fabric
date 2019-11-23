package com.mcdiamondfire.dftools;

import com.mcdiamondfire.dftools.gui.SavedToolbarGui;
import com.mcdiamondfire.dftools.screen.SavedToolbarScreen;

import org.lwjgl.glfw.GLFW;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.*;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;

public class DFTools implements ClientModInitializer {
	private static final MinecraftClient minecraft = MinecraftClient.getInstance();

	private static FabricKeyBinding savedToolbarGuiKeyBinding = FabricKeyBinding.Builder
			.create(new Identifier("dftools", "open_saved_toolbar_editor"), InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_O, "DFTools")
			.build();

	@Override
	public void onInitializeClient() {
		KeyBindingRegistry.INSTANCE.register(savedToolbarGuiKeyBinding);

		ClientTickCallback.EVENT.register(e -> {
			if (savedToolbarGuiKeyBinding.isPressed()) {
				minecraft.openScreen(new SavedToolbarScreen(new SavedToolbarGui(minecraft.getCreativeHotbarStorage())));
			}
		});
	}
}
