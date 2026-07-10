package com.flowline.workflow_engine.engine.models;

import com.flowline.workflow_engine.engine.enums.StepStatus;

import java.util.Map;

public record WorkflowResult(
        boolean success,
        String failedStepName,
        Map<String, StepStatus> finalStatuses
) {}
