package com.mcdiamondfire.dftools.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class ParserUtils {
    //The array used for the Events, first is DF Language name, second is Codeblock name.
    private static String[] eventNameTable = new String[] {
        "join|Join",
        "quit|Quit",
        "command|Command"
    };

    //The array used for the actions inside Action codeblocks, like Player Actions, etc.
    private static String[] subActionNameTable = new String[] {
        "sendMessage|SendMessage"
    };

    //The array used for the targets.
    private static String[] targetNameTable = new String[] {
        "all|AllPlayers"
    };

    public static JsonObject parseEvent(String event) {
        String dfEvent = event;
        
        for (String string : eventNameTable) {
            String[] nameTables = string.split("[|]");
            if (dfEvent.equals(nameTables[0])) {
                dfEvent = nameTables[1];
            }
        }

        if (dfEvent.equals("")) {
            return null;
        }
        
        JsonObject eventObject = new JsonObject();
        eventObject.addProperty("id", "block");
        eventObject.addProperty("block", "event");

        JsonObject arguments = new JsonObject();
        arguments.add("items", new JsonArray());
        eventObject.add("args", arguments);
        
        eventObject.addProperty("action", dfEvent);
        return eventObject;
    }

    public static JsonObject generateItem(String value, String type) {
        JsonObject itemObject = new JsonObject();
        JsonObject itemSubObject = new JsonObject();
        JsonObject itemDataObject = new JsonObject();

        if (type == "text") {
            itemDataObject.addProperty("name", value);
            itemSubObject.addProperty("id", "txt");
            itemSubObject.add("data", itemDataObject);
        }

        itemSubObject.addProperty("slot", 0);
        itemObject.add("item", itemSubObject);
        return itemObject;
    }

    public static JsonArray parseArguments(String arguments) {
        JsonArray itemsArray = new JsonArray();
        String[] argumentsArray = new String[] {arguments.replace("(", "").replace(")", "")};
        MessageUtils.warnMessage(argumentsArray[0]);
        if (arguments.contains(",")) {
            argumentsArray = arguments.replace("(", "").replace(")", "").split(",");
        }
        for (String string : argumentsArray) {
            string = string.strip();
            MessageUtils.errorMessage(string);
            if (string.startsWith("\"") && string.endsWith("\"")) {
                itemsArray.add(generateItem(string.replaceAll("\"", ""), "text"));
                System.out.println(itemsArray.toString());
            }
        }

        return itemsArray;
    }
    
    public static String parseSubaction(String subAction, String target) {
        String subActionToParse = subAction.split("[(]")[0];
        if (subActionToParse.contains(":")) {
            target = subActionToParse.split("[:]")[1];
            subActionToParse = subActionToParse.split("[:]")[0];
        }
        
        for (String string : subActionNameTable) {
            String[] nameTables = string.split("[|]");
            if (subActionToParse.equals(nameTables[0])) {
                subActionToParse = nameTables[1];
            }
        }
        
        if (target != "") {
            for (String string : targetNameTable) {
                String[] nameTables = string.split("[|]");
                if (target.equals(nameTables[0])) {
                    target = nameTables[1];
                }
            }
            return subActionToParse + ":" + target;
        } else {
            return subActionToParse;
        }
    }

    public static JsonObject parseAction(String event) {
        String dfEvent = event;
        String blockType = "block";
        String dfEventType = "";
        JsonArray itemsArray = new JsonArray();
        String dfSubAction = "";
        String target = "";

        if (dfEvent.startsWith("player.")) {
            dfEventType = "player_action";
            dfSubAction = parseSubaction(dfEvent.replace("player.", ""), target);
            if (dfSubAction.contains(":")) {
                target = dfSubAction.split("[:]")[1];
                dfSubAction = dfSubAction.split("[:]")[0];
            }
            
            itemsArray = parseArguments("(" + dfEvent.split("[(]", 2)[1]);
        }

        JsonObject actionObject = new JsonObject();
        actionObject.addProperty("id", blockType);
        actionObject.addProperty("block", dfEventType);

        JsonObject arguments = new JsonObject();
        arguments.add("items", itemsArray);
        actionObject.add("args", arguments);

        System.out.println(target);
        actionObject.addProperty("action", dfSubAction);
        if (!target.equals("")) {
            actionObject.addProperty("target", target);
        }
        return actionObject;
    }
}