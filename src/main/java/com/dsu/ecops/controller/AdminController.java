package com.dsu.ecops.controller;

import com.dsu.ecops.model.*;
import com.dsu.ecops.service.AdminService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    // Middleware to check if user is admin
    @ModelAttribute
    public void addAttributes(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);
        }
    }

    private boolean isNotAdmin(HttpSession session) {
        User user = (User) session.getAttribute("user");
        return user == null || user.getRole() != Role.ADMINISTRATOR;
    }

    @GetMapping("/stations")
    public String manageStations(HttpSession session, Model model) {
        if (isNotAdmin(session)) return "redirect:/dashboard";
        
        model.addAttribute("stations", adminService.getAllPoliceStations());
        model.addAttribute("newStation", new PoliceStation());
        return "admin/manage-stations";
    }

    @PostMapping("/stations")
    public String createStation(@ModelAttribute PoliceStation station, HttpSession session) {
        if (isNotAdmin(session)) return "redirect:/dashboard";
        
        adminService.createPoliceStation(station);
        return "redirect:/admin/stations?success=Station Created";
    }

    @GetMapping("/stations/{stationId}")
    public String viewStationDetails(@PathVariable Long stationId, HttpSession session, Model model) {
        if (isNotAdmin(session)) return "redirect:/dashboard";
        
        PoliceStation station = adminService.getPoliceStationById(stationId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid station Id"));
                
        model.addAttribute("station", station);
        model.addAttribute("newArea", new AreaOfControl());
        model.addAttribute("newDept", new Department());
        return "admin/station-details";
    }

    @PostMapping("/stations/{stationId}/areas")
    public String addAreaOfControl(@PathVariable Long stationId, @ModelAttribute AreaOfControl area, HttpSession session) {
        if (isNotAdmin(session)) return "redirect:/dashboard";
        
        adminService.addAreaOfControl(stationId, area);
        return "redirect:/admin/stations/" + stationId + "?success=Area Added";
    }

    @PostMapping("/stations/{stationId}/departments")
    public String addDepartment(@PathVariable Long stationId, @ModelAttribute Department dept, HttpSession session) {
        if (isNotAdmin(session)) return "redirect:/dashboard";
        
        adminService.addDepartment(stationId, dept);
        return "redirect:/admin/stations/" + stationId + "?success=Department Added";
    }

    @GetMapping("/assign-officers")
    public String showAssignOfficers(HttpSession session, Model model) {
        if (isNotAdmin(session)) return "redirect:/dashboard";
        
        model.addAttribute("officers", adminService.getAvailableOfficers());
        model.addAttribute("stations", adminService.getAllPoliceStations());
        return "admin/assign-officers";
    }

    @PostMapping("/assign-officers")
    public String assignOfficer(@RequestParam Long officerId, 
                                @RequestParam Long stationId, 
                                @RequestParam(required = false) Long departmentId,
                                HttpSession session) {
        if (isNotAdmin(session)) return "redirect:/dashboard";
        
        adminService.assignOfficer(officerId, stationId, departmentId);
        return "redirect:/admin/assign-officers?success=Officer Assigned";
    }
}
