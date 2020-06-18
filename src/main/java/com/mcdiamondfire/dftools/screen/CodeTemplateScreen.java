package com.mcdiamondfire.dftools.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import spinnery.widget.WInterface;
import spinnery.widget.WPanel;
import spinnery.widget.WStaticText;
import spinnery.client.screen.BaseScreen;
import spinnery.widget.WButton;
import spinnery.widget.WTextField;
import spinnery.widget.api.Position;

public class CodeTemplateScreen extends BaseScreen {
	public CodeTemplateScreen() {
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
		mainPanel.setLabel("Give Code Template");

        setIsPauseScreen(true);
        mainInterface.setClientside(true);
        mainInterface.setBlurred(true);
        
        WStaticText authorLabel = new WStaticText();
        authorLabel.getPosition().setAnchor(mainPanel).set(8, 27, 0);
        authorLabel.setText("Author:").overrideStyle("shadow", false).overrideStyle("text", 0xff3f3f3f);

		WTextField authorTextField = new WTextField();
		authorTextField.getPosition().setAnchor(mainPanel).set(84, 22, 0);
        authorTextField.getSize().setWidth(160).setHeight(18);
        authorTextField.setText(minecraft.player.getName().asFormattedString());
        
        WStaticText nameLabel = new WStaticText();
        nameLabel.getPosition().setAnchor(mainPanel).set(8, 47, 0);
        nameLabel.setText("Template Name:").overrideStyle("shadow", false).overrideStyle("text", 0xff3f3f3f);
        
        WTextField nameTextField = new WTextField();
		nameTextField.getPosition().setAnchor(mainPanel).set(84, 42, 0);
		nameTextField.getSize().setWidth(160).setHeight(18);
        
        WStaticText codeLabel = new WStaticText();
        codeLabel.getPosition().setAnchor(mainPanel).set(8, 67, 0);
        codeLabel.setText("Code:").overrideStyle("shadow", false).overrideStyle("text", 0xff3f3f3f);

        WTextField codeTextField = new WTextField();
		codeTextField.getPosition().setAnchor(mainPanel).set(84, 62, 0);
		codeTextField.getSize().setWidth(160).setHeight(18);

		WButton giveButton = new WButton();
		giveButton.getPosition().setAnchor(mainPanel).set(220, 82, 0);
		giveButton.getSize().setWidth(26).setHeight(14);
		giveButton.setLabel(new LiteralText("Give"));

		giveButton.setOnMouseClicked((widget, mouseX, mouseY, mouseButton) -> {
            String formattedName = nameTextField.getText().replaceAll("&([0-9a-fk-or]+)", "§$1").replaceAll(">>", "»").replaceAll("/»", ">>");
			minecraft.player.sendChatMessage("/dfg minecraft:ender_chest{PublicBukkitValues:{\"hypercube:codetemplatedata\":'{\"author\":\"" + authorTextField.getText() + "\",\"name\":\"" + formattedName + "\",\"version\":1,\"code\":\"" + codeTextField.getText() + "\"}'},display:{Name:'{\"text\":\"" + formattedName + "\"}'}}");
		});

		mainInterface.add(giveButton, authorLabel, authorTextField, nameLabel, nameTextField, codeLabel, codeTextField, mainPanel);
	}
}
