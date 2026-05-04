package com.dsu.ecops.service;

import com.dsu.ecops.model.*;
import com.dsu.ecops.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ComplaintService {

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private PoliceStationRepository policeStationRepository;

    @Autowired
    private UserRepository userRepository;

    public Complaint fileComplaint(Long citizenId, Long stationId, Complaint complaint) {
        User citizen = userRepository.findById(citizenId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid citizen ID"));
        PoliceStation station = policeStationRepository.findById(stationId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid station ID"));

        complaint.setCitizen(citizen);
        complaint.setPoliceStation(station);
        
        return complaintRepository.save(complaint);
    }

    public List<Complaint> getComplaintsByCitizen(Long citizenId) {
        return complaintRepository.findByCitizenId(citizenId);
    }

    public List<Complaint> getComplaintsByStation(Long stationId) {
        return complaintRepository.findByPoliceStationId(stationId);
    }
    
    public Complaint getComplaintById(Long id) {
        return complaintRepository.findById(id).orElse(null);
    }
}
