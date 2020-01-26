package com.mcdiamondfire.dftools.screen;

import com.mcdiamondfire.dftools.utils.ItemUtils;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import spinnery.client.BaseScreen;
import spinnery.widget.*;

public class GiveCommandScreen extends BaseScreen {
	public GiveCommandScreen() {
		WInterface mainInterface = new WInterface(WPosition.of(WType.FREE, 0, 0, 0), WSize.of(250, 100));
		getInterfaces().add(mainInterface);
		mainInterface.center();

		setIsPauseScreen(true);

		WStaticText staticText = new WStaticText(WPosition.of(WType.ANCHORED, 6, 6, 0, mainInterface), mainInterface, new LiteralText("Give Item"));
		staticText.setLabelShadow(true);

		WDynamicText dynamicText = new WDynamicText(WPosition.of(WType.ANCHORED, 6, 18, 0, mainInterface), WSize.of(238, 63), mainInterface);
		dynamicText.setLabel(new LiteralText("item:id{here}"));

		WButton button = new WButton(WPosition.of(WType.ANCHORED, 218, 82, 0, mainInterface), WSize.of(26, 14), mainInterface);
		button.setLabel(new LiteralText("Give"));

		mainInterface.add(staticText, dynamicText, button);
		/*
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
		*/
	}
}
