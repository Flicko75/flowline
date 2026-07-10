package com.flowline.workflow_engine.steps;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = WaitStep.class, name = "wait"),
        @JsonSubTypes.Type(value = HttpRequestStep.class, name = "http"),
        @JsonSubTypes.Type(value = ReadFileStep.class, name = "file")
})
public interface StepDefinition {

    String getName();

    Runnable toRun();

}
