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
import spinnery.widget.WPosition;
import spinnery.widget.WSize;
import spinnery.widget.WButton;
import spinnery.widget.WDynamicText;
import spinnery.widget.WStaticText;
import spinnery.widget.WType;

public class GiveCommandScreen extends BaseScreen {
	public GiveCommandScreen() {
		WInterface mainInterface = new WInterface(WPosition.of(WType.FREE, 0, 0, 0), WSize.of(250, 100));
		getInterfaces().add(mainInterface);
		mainInterface.center();

		setIsPauseScreen(true);

		WStaticText menuLabel = new WStaticText(WPosition.of(WType.ANCHORED, 6, 6, 0, mainInterface), mainInterface, new LiteralText("Give Item"));
		menuLabel.setLabelShadow(true);

		WDynamicText textBox = new WDynamicText(WPosition.of(WType.ANCHORED, 6, 18, 0, mainInterface), WSize.of(238, 63), mainInterface);
		textBox.setLabel(new LiteralText("minecraft:item_id{Tag:Here}"));

		WButton giveButton = new WButton(WPosition.of(WType.ANCHORED, 218, 82, 0, mainInterface), WSize.of(26, 14), mainInterface);
		giveButton.setLabel(new LiteralText("Give"));

		giveButton.setOnMouseClicked(() -> {
			if (giveButton.getToggleState()) {
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

		mainInterface.add(menuLabel, textBox, giveButton);
	}
}
