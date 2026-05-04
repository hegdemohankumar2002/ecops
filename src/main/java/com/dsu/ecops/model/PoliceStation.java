package com.dsu.ecops.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "police_stations")
public class PoliceStation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String district;

    @Column(nullable = false)
    private String zone;
    
    @OneToMany(mappedBy = "policeStation", cascade = CascadeType.ALL)
    private List<AreaOfControl> areasOfControl;
    
    @OneToMany(mappedBy = "policeStation", cascade = CascadeType.ALL)
    private List<Department> departments;

    public PoliceStation() {}

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public List<AreaOfControl> getAreasOfControl() {
        return areasOfControl;
    }

    public void setAreasOfControl(List<AreaOfControl> areasOfControl) {
        this.areasOfControl = areasOfControl;
    }

    public List<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(List<Department> departments) {
        this.departments = departments;
    }
}
