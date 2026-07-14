package com.flowline.workflow_ui.util;

import com.flowline.workflow_ui.models.StagedStep;

import java.util.ArrayList;
import java.util.List;

public class JsonBuilder {

    private static String buildStepObject(String type, String name, String value) {
        if (type.equals("wait")){
            return "{\"type\":\"wait\",\"name\":\"" + name + "\",\"seconds\":" + value + "}";
        } else if (type.equals("http")) {
            return "{\"type\":\"http\",\"name\":\"" + name + "\",\"url\":\"" + value + "\"}";
        } else {
            return "{\"type\":\"file\",\"name\":\"" + name + "\",\"path\":\"" + value.replace("\\", "\\\\") + "\"}";
        }
    }

    public static String buildStepsJson(List<StagedStep> stagedSteps) {
        List<String> stepObjects = new ArrayList<>();

        for (StagedStep step : stagedSteps) {
            stepObjects.add(buildStepObject(step.type(), step.name(), step.value()));
        }

        String joined = String.join(",", stepObjects);
        return "[" + joined + "]";
    }

}
