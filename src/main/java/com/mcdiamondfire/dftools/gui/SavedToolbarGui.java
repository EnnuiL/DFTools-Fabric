package com.mcdiamondfire.dftools.gui;

import com.mcdiamondfire.dftools.utils.ItemUtils;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import net.minecraft.client.options.HotbarStorage;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class SavedToolbarGui extends LightweightGuiDescription {
    public SavedToolbarGui(HotbarStorage hotbarStorage) {
        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setSize(14 * 18, 6 * 18);

        WLabel label = new WLabel("Saved Toolbar Editor");
        root.add(label, 0, 0);

        WSprite itemIcon = new WSprite(new Identifier("minecraft:textures/item/iron_pickaxe.png"));
        root.add(itemIcon, 0, 2, 1, 1);

        WTextField itemIdField = new WTextField();
        Identifier defaultItemId = Registry.ITEM.getId(hotbarStorage.getSavedHotbar(0).get(0).getItem());
        String tagToSet = hotbarStorage.getSavedHotbar(0).get(0).getOrCreateTag().toString();
        if (tagToSet.length() == 2) {
            tagToSet = "";
        }
        String amountToSet = "";
        if (hotbarStorage.getSavedHotbar(0).get(0).getCount() != 1) {
            amountToSet = " " + hotbarStorage.getSavedHotbar(0).get(0).getCount();
        }
        String textToSet = defaultItemId.toString() + tagToSet + amountToSet;
        itemIdField.setSuggestion("Item ID").setMaxLength(16384).setText(textToSet);
        root.add(itemIdField, 2, 2, 11, 1);

        WButton button = new WButton(new LiteralText("Save"));
        root.add(button, 0, 5, 4, 1);

        /*
        button.setOnClick(() -> {
            String[] itemIdArray = itemIdField.getText().split("[{]", 2);
            String[] itemIdArrayWithCount = itemIdArray[0].split("[ ]");
            Integer amount = 1;
            Identifier id = new Identifier(itemIdArrayWithCount[0]);
            ItemStack itemStack = new ItemStack(Registry.ITEM.get(id));
            
            if (itemIdArray.length == 2) {
                try {
                    CompoundTag tag = StringNbtReader.parse("{" + itemIdArray[1]);
                    itemIdArrayWithCount = itemIdArray[1].split("[ ]");
                    itemStack.setTag(tag);
                } catch (CommandSyntaxException e) {
                    e.printStackTrace();
                }
            }
            
            if (itemIdArrayWithCount.length == 2) {
                amount = Integer.getInteger(itemIdArrayWithCount[1]);
                if (amount > itemStack.getMaxCount()) {
                    amount = itemStack.getMaxCount();
                }
                itemStack.setCount(amount);
            }
            ItemUtils.setItemInHotbar(itemStack, false);
        });
        */

        button.setOnClick(() -> {
            String[] itemIdArray = itemIdField.getText().split("[{]", 2);
            String[] itemIdArrayWithAmount = itemIdField.getText().split(" (?!.* )", 2);
            String itemIdentifier = itemIdArray[0];
            String itemTag = "{}";
            String amount = "1";
            if (itemIdArray.length == 2) {
                itemTag = "{" + itemIdArray[1];
            }
            if (itemIdArrayWithAmount.length == 2) {
                if (itemTag == "{}") {
                    itemIdentifier = itemIdArrayWithAmount[0].replace(itemTag, "");
                } else {
                    itemTag = itemIdArrayWithAmount[0].replace(itemIdentifier, "");
                }
                amount = itemIdArrayWithAmount[1];
            }
            Identifier itemStackID = new Identifier(itemIdentifier);
            ItemStack itemStack = new ItemStack(Registry.ITEM.get(itemStackID));
            if (itemIdArray.length == 2) {
                try {
                    CompoundTag itemStackTag = StringNbtReader.parse(itemTag);
                    itemStack.setTag(itemStackTag);
                } catch (CommandSyntaxException e) {
                    e.printStackTrace();
                }
            }
            Integer itemStackAmount = Integer.decode(amount);
            if (itemStackAmount >= itemStack.getMaxCount()) {
                itemStackAmount = itemStack.getMaxCount();
            }
            itemStack.setCount(itemStackAmount);

            ItemUtils.setItemInHotbar(itemStack, false);
        });

        root.validate(this);
    }
}
