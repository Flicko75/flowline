package com.flowline.workflow_engine.engine.models;

import com.flowline.workflow_engine.engine.enums.StepStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "steps")
public class StepEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int stepOrder;

    private String definitionJson;

    @Enumerated(EnumType.STRING)
    private StepStatus status;

}
