package com.dsu.ecops.repository;

import com.dsu.ecops.model.OfficerAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfficerAssignmentRepository extends JpaRepository<OfficerAssignment, Long> {
    List<OfficerAssignment> findByPoliceStationId(Long policeStationId);
    List<OfficerAssignment> findByUserId(Long userId);
}
