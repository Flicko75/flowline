package com.flowline.workflow_engine.engine;

import com.flowline.workflow_engine.engine.models.ProgressMessage;
import com.flowline.workflow_engine.engine.models.Step;
import com.flowline.workflow_engine.engine.enums.StepStatus;
import com.flowline.workflow_engine.engine.models.WorkflowResult;
import com.flowline.workflow_engine.handlers.ProgressWebSocketHandler;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

@RequiredArgsConstructor
public class Engine {

    private final ProgressWebSocketHandler handler;

    public WorkflowResult run(List<Step> steps) {
        Map<String, StepStatus> statuses = new LinkedHashMap<>();

        for (Step step : steps) {
            try {
                statuses.put(step.getName(), StepStatus.RUNNING);
                handler.sendUpdate(new ProgressMessage(step.getName(), StepStatus.RUNNING));
                try {
                    step.getTask().run();
                    statuses.put(step.getName(), StepStatus.COMPLETED);
                    handler.sendUpdate(new ProgressMessage(step.getName(), StepStatus.COMPLETED));
                } catch (Exception e) {
                    statuses.put(step.getName(), StepStatus.FAILED);
                    handler.sendUpdate(new ProgressMessage(step.getName(), StepStatus.FAILED));
                    return new WorkflowResult(false, step.getName(), statuses);
                }
            } catch (IOException e) {
                System.out.println("Failed to send progress update for step: " + step.getName());
                e.printStackTrace();
            }
        }
        return new WorkflowResult(true, null, statuses);
    }
}
