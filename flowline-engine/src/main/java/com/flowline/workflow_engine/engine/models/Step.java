package com.flowline.workflow_engine.engine.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Step {
    private final String name;
    private final Runnable task;
}
