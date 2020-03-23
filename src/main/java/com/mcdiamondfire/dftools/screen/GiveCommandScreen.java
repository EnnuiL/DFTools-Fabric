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
import spinnery.widget.WInterface;
import spinnery.widget.WPanel;
import spinnery.widget.WButton;
import spinnery.widget.WTextField;
import spinnery.widget.api.Position;

public class GiveCommandScreen extends BaseScreen {
	public GiveCommandScreen() {
		super();

		WInterface mainInterface = getInterface();
		
		WPanel mainPanel = new WPanel();
		mainPanel.getSize().setWidth(250).setHeight(100);
		//mainPanel.center();
		mainPanel.setPosition(Position.of(mainPanel.getPosition())
			.setX(mainInterface.getX() + mainInterface.getWidth() / 2 - mainPanel.getWidth() / 2)
			.setY(mainInterface.getY() + mainInterface.getHeight() / 2 - mainPanel.getHeight() / 2)
		);
		mainPanel.setLabel("Give Item");

		setIsPauseScreen(true);
		mainInterface.setBlurred(true);

		WTextField textBox = new WTextField();
		textBox.getPosition().setAnchor(mainPanel).set(6, 18, 0);
		textBox.getSize().setWidth(238).setHeight(63);
		textBox.setLabel(new LiteralText("minecraft:item_id{Tag:Here}"));

		WButton giveButton = new WButton();
		giveButton.getPosition().setAnchor(mainPanel).set(218, 82, 0);
		giveButton.getSize().setWidth(26).setHeight(14);
		giveButton.setLabel(new LiteralText("Give"));

		giveButton.setOnMouseClicked((WButton w, int mouseX, int mouseY, int mouseButton) -> {
			String[] itemIdArray = textBox.getText().split("[{]", 2);
			String[] itemIdArrayWithAmount = textBox.getText().split(" (?!.* )", 2);
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

		mainInterface.add(giveButton, textBox, mainPanel);
	}
}
