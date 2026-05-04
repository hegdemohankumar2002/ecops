package com.dsu.ecops.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "case_files")
public class CaseFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "complaint_id", nullable = false)
    private Complaint complaint;

    @Column(columnDefinition = "TEXT")
    private String firDetails;

    @Column(columnDefinition = "TEXT")
    private String chargeSheet;

    @Column(columnDefinition = "TEXT")
    private String propertySeizure;

    private String courtDisposalStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WarrantStatus warrantStatus;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;

    public CaseFile() {
        this.warrantStatus = WarrantStatus.PENDING;
        this.lastUpdated = new Date();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Complaint getComplaint() {
        return complaint;
    }

    public void setComplaint(Complaint complaint) {
        this.complaint = complaint;
    }

    public String getFirDetails() {
        return firDetails;
    }

    public void setFirDetails(String firDetails) {
        this.firDetails = firDetails;
    }

    public String getChargeSheet() {
        return chargeSheet;
    }

    public void setChargeSheet(String chargeSheet) {
        this.chargeSheet = chargeSheet;
    }

    public String getPropertySeizure() {
        return propertySeizure;
    }

    public void setPropertySeizure(String propertySeizure) {
        this.propertySeizure = propertySeizure;
    }

    public String getCourtDisposalStatus() {
        return courtDisposalStatus;
    }

    public void setCourtDisposalStatus(String courtDisposalStatus) {
        this.courtDisposalStatus = courtDisposalStatus;
    }

    public WarrantStatus getWarrantStatus() {
        return warrantStatus;
    }

    public void setWarrantStatus(WarrantStatus warrantStatus) {
        this.warrantStatus = warrantStatus;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
