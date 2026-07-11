package com.flowline.workflow_engine.steps;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class WaitStep implements StepDefinition {
    private String name;
    private int seconds;

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
