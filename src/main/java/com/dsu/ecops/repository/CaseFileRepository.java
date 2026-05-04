package com.dsu.ecops.repository;

import com.dsu.ecops.model.CaseFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CaseFileRepository extends JpaRepository<CaseFile, Long> {
    Optional<CaseFile> findByComplaintId(Long complaintId);
    
    @org.springframework.data.jpa.repository.Query("SELECT c FROM CaseFile c WHERE " +
            "LOWER(c.complaint.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.complaint.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.firDetails) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.chargeSheet) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<CaseFile> searchIntelligence(String keyword);
}
