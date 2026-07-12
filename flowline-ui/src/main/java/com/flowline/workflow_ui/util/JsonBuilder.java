package com.flowline.workflow_ui.util;

public class JsonBuilder {

    public static String buildSingleStepJson(String type, String name, String value) {
        if (type.equals("wait")){
            return "[{\"type\":\"wait\",\"name\":\"" + name + "\",\"seconds\":" + value + "}]";
        } else if (type.equals("http")) {
            return "[{\"type\":\"http\",\"name\":\"" + name + "\",\"url\":\"" + value + "\"}]";
        } else {
            return "[{\"type\":\"file\",\"name\":\"" + name + "\",\"path\":\"" + value.replace("\\", "\\\\") + "\"}]";
        }
    }

}
