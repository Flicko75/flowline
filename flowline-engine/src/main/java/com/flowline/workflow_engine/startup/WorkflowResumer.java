package com.flowline.workflow_engine.startup;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowline.workflow_engine.engine.Engine;
import com.flowline.workflow_engine.engine.enums.StepStatus;
import com.flowline.workflow_engine.engine.models.Step;
import com.flowline.workflow_engine.engine.models.StepEntity;
import com.flowline.workflow_engine.handlers.ProgressWebSocketHandler;
import com.flowline.workflow_engine.repos.StepRepository;
import com.flowline.workflow_engine.steps.StepDefinition;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
@RequiredArgsConstructor
public class WorkflowResumer implements CommandLineRunner {

    private final StepRepository stepRepository;

    private final ObjectMapper objectMapper;

    private final ProgressWebSocketHandler handler;

    @Override
    public void run(String... args) {
        List<StepEntity> stepEntities = stepRepository.findAll();

        if (stepEntities.isEmpty()) {
            return;
        }

        stepEntities.sort(Comparator.comparingInt(StepEntity::getStepOrder));

        List<StepDefinition> stepDefinitions = new ArrayList<>();
        for (StepEntity entity : stepEntities) {
            try {
                stepDefinitions.add(
                        objectMapper.readValue(entity.getDefinitionJson(), StepDefinition.class)
                );
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

        List<Step> steps = stepDefinitions.stream()
                .map(def -> new Step(def.getName(), def.toRun()))
                .toList();

        int resumeIdx = 0;
        for (int i=0; i<stepEntities.size(); i++) {
            if (stepEntities.get(i).getStatus() != StepStatus.COMPLETED) {
                resumeIdx = i;
                break;
            }
        }

        boolean allCompleted = stepEntities.stream()
                .allMatch(e -> e.getStatus() == StepStatus.COMPLETED);

        if (allCompleted) {
            stepRepository.deleteAll();
            return;
        }

        List<Step> stepsToRun = steps.subList(resumeIdx, steps.size());
        List<StepEntity> entitiesToRun = stepEntities.subList(resumeIdx, stepEntities.size());

        Engine engine = new Engine(handler, stepRepository);
        engine.run(stepsToRun, entitiesToRun);
    }

}
