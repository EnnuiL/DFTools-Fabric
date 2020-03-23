package com.mcdiamondfire.dftools.screen;

import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import spinnery.client.BaseScreen;
import spinnery.widget.WInterface;
import spinnery.widget.WPanel;
import spinnery.widget.WStaticText;
import spinnery.widget.WButton;
import spinnery.widget.WTextField;
import spinnery.widget.api.Position;

public class CodeTemplateScreen extends BaseScreen {
	public CodeTemplateScreen() {
		super();

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
        mainInterface.setBlurred(true);
        
        WStaticText authorLabel = new WStaticText();
        authorLabel.getPosition().setAnchor(mainPanel).set(6, 26, 0);
        authorLabel.setText("Author:").setTheme(new Identifier("dftools", "dftools"));

		WTextField authorTextField = new WTextField();
		authorTextField.getPosition().setAnchor(mainPanel).set(82, 22, 0);
        authorTextField.getSize().setWidth(162).setHeight(16);
        
        WStaticText nameLabel = new WStaticText();
        nameLabel.getPosition().setAnchor(mainPanel).set(6, 46, 0);
        nameLabel.setText("Template Name:").setTheme(new Identifier("dftools", "dftools"));
        
        WTextField nameTextField = new WTextField();
		nameTextField.getPosition().setAnchor(mainPanel).set(82, 42, 0);
		nameTextField.getSize().setWidth(162).setHeight(16);
        
        WStaticText codeLabel = new WStaticText();
        codeLabel.getPosition().setAnchor(mainPanel).set(6, 66, 0);
        codeLabel.setText("Code:").setTheme(new Identifier("dftools", "dftools"));

        WTextField codeTextField = new WTextField();
		codeTextField.getPosition().setAnchor(mainPanel).set(82, 62, 0);
		codeTextField.getSize().setWidth(162).setHeight(16);

		WButton giveButton = new WButton();
		giveButton.getPosition().setAnchor(mainPanel).set(220, 82, 0);
		giveButton.getSize().setWidth(26).setHeight(14);
		giveButton.setLabel(new LiteralText("Give"));

		giveButton.setOnMouseClicked((WButton w, int mouseX, int mouseY, int mouseButton) -> {
            String formattedName = nameTextField.getText().replaceAll("&([0-9a-fk-or]+)", "§$1").replaceAll(">>", "»").replaceAll("/»", ">>");
			minecraft.player.sendChatMessage("/dfg minecraft:ender_chest{PublicBukkitValues:{\"hypercube:codetemplatedata\":'{\"author\":\"" + authorTextField.getText() + "\",\"name\":\"" + formattedName + "\",\"version\":1,\"code\":\"" + codeTextField.getText() + "\"}'},display:{Name:'{\"text\":\"" + formattedName + "\"}'}}");
		});

		mainInterface.add(giveButton, authorLabel, authorTextField, nameLabel, nameTextField, codeLabel, codeTextField, mainPanel);
	}
}
