package com.flowline.workflow_engine.services;

import com.flowline.workflow_engine.engine.Engine;
import com.flowline.workflow_engine.engine.models.Step;
import com.flowline.workflow_engine.engine.models.WorkflowResult;
import com.flowline.workflow_engine.steps.StepDefinition;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkflowService {

    public WorkflowResult runWorkflow(List<StepDefinition> stepDefinitions) {
        List<Step> steps = stepDefinitions.stream()
                .map(def -> new Step(def.getName(), def.toRun()))
                .toList();

        Engine engine = new Engine();
        return engine.run(steps);
    }

}
