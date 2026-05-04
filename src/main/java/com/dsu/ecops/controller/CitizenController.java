package com.dsu.ecops.controller;

import com.dsu.ecops.model.*;
import com.dsu.ecops.service.AdminService;
import com.dsu.ecops.service.ComplaintService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/citizen")
public class CitizenController {

    @Autowired
    private ComplaintService complaintService;
    
    @Autowired
    private AdminService adminService; // Reuse to fetch stations

    @ModelAttribute
    public void addAttributes(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);
        }
    }

    private boolean isNotCitizen(HttpSession session) {
        User user = (User) session.getAttribute("user");
        return user == null || user.getRole() != Role.CITIZEN;
    }

    @GetMapping("/file-complaint")
    public String showFileComplaintForm(HttpSession session, Model model) {
        if (isNotCitizen(session)) return "redirect:/dashboard";
        
        model.addAttribute("complaint", new Complaint());
        model.addAttribute("stations", adminService.getAllPoliceStations());
        return "citizen/file-complaint";
    }

    @PostMapping("/file-complaint")
    public String submitComplaint(@ModelAttribute Complaint complaint, @RequestParam Long stationId, HttpSession session) {
        if (isNotCitizen(session)) return "redirect:/dashboard";
        
        User citizen = (User) session.getAttribute("user");
        complaintService.fileComplaint(citizen.getId(), stationId, complaint);
        
        return "redirect:/citizen/my-complaints?success=Complaint Filed Successfully";
    }

    @GetMapping("/my-complaints")
    public String viewMyComplaints(HttpSession session, Model model) {
        if (isNotCitizen(session)) return "redirect:/dashboard";
        
        User citizen = (User) session.getAttribute("user");
        model.addAttribute("complaints", complaintService.getComplaintsByCitizen(citizen.getId()));
        return "citizen/my-complaints";
    }
}
