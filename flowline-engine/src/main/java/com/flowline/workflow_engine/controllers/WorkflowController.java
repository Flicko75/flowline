package com.flowline.workflow_engine.controllers;

import com.flowline.workflow_engine.engine.Engine;
import com.flowline.workflow_engine.engine.models.Step;
import com.flowline.workflow_engine.engine.models.WorkflowResult;
import com.flowline.workflow_engine.services.WorkflowService;
import com.flowline.workflow_engine.steps.StepDefinition;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/workflows")
@RequiredArgsConstructor
public class WorkflowController {

    private final WorkflowService workflowService;

    @PostMapping("/run")
    public WorkflowResult run(@RequestBody List<StepDefinition> stepDefinitions){
        return workflowService.runWorkflow(stepDefinitions);
    }

}
