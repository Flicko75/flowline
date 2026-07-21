package com.flowline.workflow_engine.engine;

import com.flowline.workflow_engine.engine.models.ProgressMessage;
import com.flowline.workflow_engine.engine.models.Step;
import com.flowline.workflow_engine.engine.enums.StepStatus;
import com.flowline.workflow_engine.engine.models.StepEntity;
import com.flowline.workflow_engine.engine.models.WorkflowResult;
import com.flowline.workflow_engine.handlers.ProgressWebSocketHandler;
import com.flowline.workflow_engine.repos.StepRepository;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

@RequiredArgsConstructor
public class Engine {

    private final ProgressWebSocketHandler handler;

    private final StepRepository stepRepository;

    public WorkflowResult run(List<Step> steps, List<StepEntity> entities) {
        Map<String, StepStatus> statuses = new LinkedHashMap<>();

        for (int i=0; i<steps.size(); i++) {
            Step step = steps.get(i);
            StepEntity entity = entities.get(i);

            try {
                statuses.put(step.getName(), StepStatus.RUNNING);
                handler.sendUpdate(new ProgressMessage(step.getName(), StepStatus.RUNNING));
                entity.setStatus(StepStatus.RUNNING);
                stepRepository.save(entity);

                try {
                    step.getTask().run();
                    statuses.put(step.getName(), StepStatus.COMPLETED);
                    handler.sendUpdate(new ProgressMessage(step.getName(), StepStatus.COMPLETED));
                    entity.setStatus(StepStatus.COMPLETED);
                    stepRepository.save(entity);

                } catch (Exception e) {
                    statuses.put(step.getName(), StepStatus.FAILED);
                    handler.sendUpdate(new ProgressMessage(step.getName(), StepStatus.FAILED));
                    entity.setStatus(StepStatus.FAILED);
                    stepRepository.save(entity);

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
