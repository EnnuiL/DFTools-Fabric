package com.mcdiamondfire.dftools.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;
import spinnery.client.BaseScreen;
import spinnery.widget.WInterface;
import spinnery.widget.WPanel;
import spinnery.widget.WButton;
import spinnery.widget.WTextArea;
import spinnery.widget.api.Position;

public class GiveCommandScreen extends BaseScreen {
	public GiveCommandScreen() {
		super();
		
		MinecraftClient minecraft = MinecraftClient.getInstance();

		WInterface mainInterface = getInterface();
		
		WPanel mainPanel = new WPanel();
		mainPanel.getSize().setWidth(250).setHeight(100);
		//mainPanel.center();
        mainPanel.setPosition(Position.of(mainPanel.getPosition())
            .setX(mainInterface.getX() + mainInterface.getWidth() / 2 - mainPanel.getWidth() / 2)
            .setY(mainInterface.getY() + mainInterface.getHeight() / 2 - mainPanel.getHeight() / 2)
        );
        mainPanel.setOnAlign((WPanel panel) -> {
            panel.setPosition(Position.of(mainPanel.getPosition())
                .setX(mainInterface.getX() + mainInterface.getWidth() / 2 - mainPanel.getWidth() / 2)
                .setY(mainInterface.getY() + mainInterface.getHeight() / 2 - mainPanel.getHeight() / 2)
            );
        });
		mainPanel.setLabel("Give Item");

		setIsPauseScreen(true);
		mainInterface.setBlurred(true);

		WTextArea textBox = new WTextArea().setLineWrap(true);
		textBox.getPosition().setAnchor(mainPanel).set(6, 18, 0);
		textBox.getSize().setWidth(238).setHeight(63);
		textBox.setLabel(new LiteralText("minecraft:item_id{Tag:Here}"));

		WButton giveButton = new WButton();
		giveButton.getPosition().setAnchor(mainPanel).set(220, 82, 0);
		giveButton.getSize().setWidth(26).setHeight(14);
		giveButton.setLabel(new LiteralText("Give"));

		giveButton.setOnMouseClicked((WButton w, int mouseX, int mouseY, int mouseButton) -> {
			minecraft.player.sendChatMessage("/dfg " + textBox.getText());
		});

		mainInterface.add(giveButton, textBox, mainPanel);
	}
}
