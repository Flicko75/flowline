package com.flowline.workflow_engine.steps;

public class WaitStep implements StepDefinition {
    private String name;
    private int seconds;

    public String getName() {
        return name;
    }

    public Runnable toRun() {
        return () -> {
            try {
                Thread.sleep(seconds * 1000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };
    }

}
