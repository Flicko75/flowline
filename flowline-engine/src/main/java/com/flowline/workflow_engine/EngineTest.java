package com.flowline.workflow_engine;

import java.util.List;

public class EngineTest {
    public static void main(String[] args) {
        Step step1 = new Step("step1", () -> System.out.println("Running step 1"));
        Step step2 = new Step("step2", () -> { throw new RuntimeException("boom"); });
        Step step3 = new Step("step3", () -> System.out.println("Running step 3"));

        Engine engine = new Engine();
        WorkflowResult result = engine.run(List.of(step1, step2, step3));

        System.out.println(result);
    }
}
