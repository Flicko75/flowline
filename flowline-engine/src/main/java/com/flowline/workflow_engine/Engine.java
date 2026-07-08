package com.flowline.workflow_engine;

import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

public class Engine {
    public WorkflowResult run(List<Step> steps) {
        Map<String, StepStatus> statuses = new LinkedHashMap<>();

        for (Step step : steps) {
            statuses.put(step.getName(), StepStatus.RUNNING);
            try {
                step.getTask().run();
                statuses.put(step.getName(), StepStatus.COMPLETED);
            } catch (Exception e) {
                statuses.put(step.getName(), StepStatus.FAILED);
                return new WorkflowResult(false, step.getName(), statuses);
            }
        }
        return new WorkflowResult(true, null, statuses);
    }
}
