package com.flowline.workflow_engine.engine.models;

public class Step {
    private final String name;
    private final Runnable task;

    public Step(String name, Runnable task) {
        this.name = name;
        this.task = task;
    }

    public String getName() {
        return name;
    }

    public Runnable getTask() {
        return task;
    }
}
