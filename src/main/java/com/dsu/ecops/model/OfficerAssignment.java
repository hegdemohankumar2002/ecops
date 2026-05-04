package com.dsu.ecops.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "officer_assignments")
public class OfficerAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // The Officer

    @ManyToOne
    @JoinColumn(name = "police_station_id", nullable = false)
    private PoliceStation policeStation;

    @ManyToOne
    @JoinColumn(name = "department_id") // Can be null if assigned generally to the station
    private Department department;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date assignedDate;

    public OfficerAssignment() {
        this.assignedDate = new Date();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public PoliceStation getPoliceStation() {
        return policeStation;
    }

    public void setPoliceStation(PoliceStation policeStation) {
        this.policeStation = policeStation;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Date getAssignedDate() {
        return assignedDate;
    }

    public void setAssignedDate(Date assignedDate) {
        this.assignedDate = assignedDate;
    }
}
