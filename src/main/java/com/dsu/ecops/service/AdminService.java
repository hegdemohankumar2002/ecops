package com.dsu.ecops.service;

import com.dsu.ecops.model.*;
import com.dsu.ecops.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    private PoliceStationRepository policeStationRepository;

    @Autowired
    private AreaOfControlRepository areaOfControlRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private OfficerAssignmentRepository officerAssignmentRepository;
    
    @Autowired
    private UserRepository userRepository;

    public List<PoliceStation> getAllPoliceStations() {
        return policeStationRepository.findAll();
    }

    public Optional<PoliceStation> getPoliceStationById(Long id) {
        return policeStationRepository.findById(id);
    }

    public PoliceStation createPoliceStation(PoliceStation station) {
        return policeStationRepository.save(station);
    }

    @Transactional
    public AreaOfControl addAreaOfControl(Long stationId, AreaOfControl area) {
        PoliceStation station = policeStationRepository.findById(stationId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid station ID"));
        area.setPoliceStation(station);
        return areaOfControlRepository.save(area);
    }

    @Transactional
    public Department addDepartment(Long stationId, Department department) {
        PoliceStation station = policeStationRepository.findById(stationId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid station ID"));
        department.setPoliceStation(station);
        return departmentRepository.save(department);
    }

    @Transactional
    public OfficerAssignment assignOfficer(Long officerId, Long stationId, Long departmentId) {
        User officer = userRepository.findById(officerId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid officer ID"));
        
        PoliceStation station = policeStationRepository.findById(stationId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid station ID"));
                
        Department department = null;
        if (departmentId != null) {
            department = departmentRepository.findById(departmentId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid department ID"));
        }

        OfficerAssignment assignment = new OfficerAssignment();
        assignment.setUser(officer);
        assignment.setPoliceStation(station);
        assignment.setDepartment(department);

        return officerAssignmentRepository.save(assignment);
    }
    
    public List<User> getAvailableOfficers() {
        // Return all officers/detectives. In real app, filter those who are not already assigned.
        return userRepository.findAll().stream()
                .filter(u -> u.getRole() == Role.POLICE_OFFICER || u.getRole() == Role.DETECTIVE)
                .toList();
    }
}
