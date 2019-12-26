package com.mcdiamondfire.dftools.gui;

import com.mcdiamondfire.dftools.utils.ItemUtils;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class GiveCommandGui extends LightweightGuiDescription {
    public GiveCommandGui() {
        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setSize(14 * 18, 6 * 18);

        WLabel label = new WLabel("Give Item");
        root.add(label, 0, 0);

        WSprite itemIcon = new WSprite(new Identifier("minecraft:textures/item/name_tag.png"));
        root.add(itemIcon, 0, 2, 1, 1);

        WTextField itemIdField = new WTextField();
        itemIdField.setSuggestion("item:id{here}").setMaxLength(16384);
        root.add(itemIdField, 2, 2, 11, 1);

        WButton button = new WButton(new LiteralText("Give"));
        root.add(button, 0, 5, 4, 1);
        
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
            int itemStackAmount = Integer.parseInt(amount);
            if (itemStackAmount >= itemStack.getMaxCount()) {
                itemStackAmount = itemStack.getMaxCount();
            }
            itemStack.setCount(itemStackAmount);

            ItemUtils.setItemInHotbar(itemStack, false);
        });

        root.validate(this);
    }
}
