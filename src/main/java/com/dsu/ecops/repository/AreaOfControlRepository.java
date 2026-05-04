package com.dsu.ecops.repository;

import com.dsu.ecops.model.AreaOfControl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AreaOfControlRepository extends JpaRepository<AreaOfControl, Long> {
    List<AreaOfControl> findByPoliceStationId(Long policeStationId);
}
