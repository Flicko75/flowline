package com.flowline.workflow_engine.engine.models;

import com.flowline.workflow_engine.engine.enums.StepStatus;

public record ProgressMessage(
        String stepName,
        StepStatus status
) {}
