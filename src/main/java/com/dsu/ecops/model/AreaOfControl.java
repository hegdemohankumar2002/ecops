package com.dsu.ecops.model;

import jakarta.persistence.*;

@Entity
@Table(name = "areas_of_control")
public class AreaOfControl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String areaName;

    @Column(nullable = false)
    private String zipCode;

    @ManyToOne
    @JoinColumn(name = "police_station_id", nullable = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private PoliceStation policeStation;

    public AreaOfControl() {}

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public PoliceStation getPoliceStation() {
        return policeStation;
    }

    public void setPoliceStation(PoliceStation policeStation) {
        this.policeStation = policeStation;
    }
}
