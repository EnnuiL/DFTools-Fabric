package com.mcdiamondfire.dftools.gui;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.HotbarStorage;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.command.arguments.ItemStackArgument;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class SavedToolbarGui extends LightweightGuiDescription {
    public SavedToolbarGui(HotbarStorage hotbarStorage) {
        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setSize(14*18, 8*18);

        WLabel label = new WLabel("Saved Toolbar Editor");
        root.add(label, 0, 0);

        WSprite itemIcon = new WSprite(new Identifier("minecraft:textures/item/iron_pickaxe.png"));
        root.add(itemIcon, 0, 2, 1, 1);
        
        WTextField itemIdField = new WTextField();
        Identifier defaultItemId = Registry.ITEM.getId(hotbarStorage.getSavedHotbar(0).get(0).getItem());
        itemIdField.setSuggestion("Item ID").setMaxLength(128).setText(defaultItemId.getNamespace() + ":" + defaultItemId.getPath());
        root.add(itemIdField, 2, 2, 11, 1);

        WSprite nameTagIcon = new WSprite(new Identifier("minecraft:textures/item/name_tag.png"));
        root.add(nameTagIcon, 0, 4, 1, 1);
        
        WTextField nbtField = new WTextField();
        nbtField.setSuggestion("Item NBT").setMaxLength(4096).setText(hotbarStorage.getSavedHotbar(0).get(0).getOrCreateTag().asString());
        root.add(nbtField, 2, 4, 11, 1);

        WButton button = new WButton(new LiteralText("Save"));
        root.add(button, 0, 7, 4, 1);

        button.setOnClick(() -> {
            
        });

        root.validate(this);
    }
}
