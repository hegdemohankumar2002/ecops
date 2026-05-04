package com.dsu.ecops.repository;

import com.dsu.ecops.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    List<Department> findByPoliceStationId(Long policeStationId);
}
