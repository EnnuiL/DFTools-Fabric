package com.mcdiamondfire.dftools.screen;

import com.mcdiamondfire.dftools.utils.ItemUtils;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.client.MinecraftClient;
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
import spinnery.widget.WStaticText;
import spinnery.widget.WTextField;
import spinnery.widget.api.Position;
import spinnery.widget.api.Size;

public class GiveCommandScreen extends BaseScreen {
	public GiveCommandScreen() {
		super();
		
		MinecraftClient minecraftClient = MinecraftClient.getInstance();

		WInterface mainInterface = getInterface();
		
		WPanel mainPanel = mainInterface.getFactory().build(WPanel.class);
		mainPanel.getSize().setWidth(250);
		mainPanel.getSize().setHeight(100);
		
		mainPanel.setLabel("Give Item");
		mainPanel.center();

		setIsPauseScreen(true);
		mainInterface.setBlurred(true);

		//WStaticText menuLabel = new WStaticText(WPosition.of(WType.ANCHORED, 6, 6, 0, mainInterface), mainInterface, new LiteralText("Give Item"));
		//menuLabel.setLabelShadow(true);

		WTextField textBox = new WTextField();
		textBox.getPosition().set(238, 63, 0);
		textBox.getSize().setWidth(6).setHeight(18);
		textBox.setLabel(new LiteralText("minecraft:item_id{Tag:Here}"));

		/*
		//WButton giveButton = new WButton(WPosition.of(WType.ANCHORED, 218, 82, 0, mainInterface), WSize.of(26, 14), mainInterface);
		WButton giveButton = new WButton();
		giveButton.getPosition().set(218, 82, 0);
		giveButton.getSize().setWidth(26).setHeight(14);
		giveButton.setLabel(new LiteralText("Give"));

		giveButton.onMouseClicked((int)minecraft.mouse.getX(), (int)minecraft.mouse.getY(), 0) -> {};

		giveButton.setOnMouseClicked(() -> {
			if (giveButton.isLowered()) {
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
			}
		});
		*/

		mainInterface.add(mainPanel, textBox);
	}
}
