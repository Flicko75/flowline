package com.flowline.workflow_engine;

import java.util.Map;

public record WorkflowResult(
        boolean success,
        String failedStepName,
        Map<String, StepStatus> finalStatuses
) {}
