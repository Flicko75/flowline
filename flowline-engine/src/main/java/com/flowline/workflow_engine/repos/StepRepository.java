package com.flowline.workflow_engine.repos;

import com.flowline.workflow_engine.engine.models.StepEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StepRepository extends JpaRepository<StepEntity, Long> {

}
