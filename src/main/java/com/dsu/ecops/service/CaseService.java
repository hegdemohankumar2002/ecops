package com.dsu.ecops.service;

import com.dsu.ecops.model.*;
import com.dsu.ecops.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class CaseService {

    @Autowired
    private CaseFileRepository caseFileRepository;

    @Autowired
    private ComplaintRepository complaintRepository;

    @Transactional
    public CaseFile fileFIR(Long complaintId, String firDetails) {
        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid complaint ID"));
                
        if (complaint.getStatus() == ComplaintStatus.FIR_FILED) {
            throw new IllegalStateException("FIR already filed for this complaint.");
        }

        complaint.setStatus(ComplaintStatus.FIR_FILED);
        complaintRepository.save(complaint);

        CaseFile caseFile = new CaseFile();
        caseFile.setComplaint(complaint);
        caseFile.setFirDetails(firDetails);
        
        return caseFileRepository.save(caseFile);
    }
    
    @Transactional
    public void rejectComplaint(Long complaintId) {
        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid complaint ID"));
        complaint.setStatus(ComplaintStatus.REJECTED);
        complaintRepository.save(complaint);
    }

    public CaseFile getCaseFileById(Long caseId) {
        return caseFileRepository.findById(caseId).orElse(null);
    }
    
    public CaseFile getCaseFileByComplaintId(Long complaintId) {
        return caseFileRepository.findByComplaintId(complaintId).orElse(null);
    }

    public List<CaseFile> getAllCases() {
        return caseFileRepository.findAll();
    }

    @Transactional
    public CaseFile updateCaseDetails(Long caseId, CaseFile updates) {
        CaseFile existing = caseFileRepository.findById(caseId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid case ID"));

        if (updates.getChargeSheet() != null) existing.setChargeSheet(updates.getChargeSheet());
        if (updates.getPropertySeizure() != null) existing.setPropertySeizure(updates.getPropertySeizure());
        if (updates.getCourtDisposalStatus() != null) existing.setCourtDisposalStatus(updates.getCourtDisposalStatus());
        
        existing.setLastUpdated(new Date());
        return caseFileRepository.save(existing);
    }

    @Transactional
    public CaseFile updateWarrantStatus(Long caseId, WarrantStatus status) {
        CaseFile existing = caseFileRepository.findById(caseId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid case ID"));
                
        existing.setWarrantStatus(status);
        existing.setLastUpdated(new Date());
        return caseFileRepository.save(existing);
    }

    public List<CaseFile> searchIntelligence(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return List.of();
        }
        return caseFileRepository.searchIntelligence(keyword);
    }
}
