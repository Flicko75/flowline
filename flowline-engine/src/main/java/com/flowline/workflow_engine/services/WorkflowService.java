package com.flowline.workflow_engine.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowline.workflow_engine.engine.Engine;
import com.flowline.workflow_engine.engine.enums.StepStatus;
import com.flowline.workflow_engine.engine.models.Step;
import com.flowline.workflow_engine.engine.models.StepEntity;
import com.flowline.workflow_engine.engine.models.WorkflowResult;
import com.flowline.workflow_engine.handlers.ProgressWebSocketHandler;
import com.flowline.workflow_engine.repos.StepRepository;
import com.flowline.workflow_engine.steps.StepDefinition;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkflowService {

    private final ProgressWebSocketHandler handler;

    private final StepRepository stepRepository;

    private final ObjectMapper objectMapper;

    public WorkflowResult runWorkflow(List<StepDefinition> stepDefinitions) {
        List<Step> steps = stepDefinitions.stream()
                .map(def -> new Step(def.getName(), def.toRun()))
                .toList();

        List<StepEntity> entities = new ArrayList<>();

        for (int i=0; i<stepDefinitions.size(); i++) {
            StepDefinition def = stepDefinitions.get(i);
            StepEntity entity = new StepEntity();

            entity.setStepOrder(i);
            try {
                entity.setDefinitionJson(objectMapper.writeValueAsString(def));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            entity.setStatus(StepStatus.NOT_STARTED);

            entities.add(entity);
            stepRepository.save(entity);
        }

        Engine engine = new Engine(handler, stepRepository);

        WorkflowResult result =  engine.run(steps, entities);
        if (result.success()) {
            stepRepository.deleteAll();
        }

        return result;
    }

}
