package com.mcdiamondfire.dftools.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class ParserUtils {
    private static String[] eventNameTable = new String[] {
        "command|Command",
        "join|Join",
        "quit|Quit"
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

    public static void parseSubaction(String subAction, String target) {
        String subActionToParse = subAction.split("[(]")[0];
        if (subActionToParse.contains(":")) {
            target = subActionToParse.split("[:]")[1];
            subActionToParse = subActionToParse.split("[:]")[0];
        }
        System.out.println(subActionToParse);
    }

    public static JsonObject parseAction(String event) {
        String dfEvent = event;
        String blockType = "block";
        String dfEventType = "";
        String target = "";

        if (dfEvent.startsWith("player.")) {
            dfEventType = "player_action";
            parseSubaction(dfEvent.replace("player.", ""), target);
        }

        JsonObject actionObject = new JsonObject();
        actionObject.addProperty("id", blockType);
        actionObject.addProperty("block", dfEventType);
        return actionObject;
    }
}