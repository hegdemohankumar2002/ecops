package com.dsu.ecops.controller;

import com.dsu.ecops.model.*;
import com.dsu.ecops.repository.OfficerAssignmentRepository;
import com.dsu.ecops.service.CaseService;
import com.dsu.ecops.service.ComplaintService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/officer")
public class OfficerController {

    @Autowired
    private ComplaintService complaintService;

    @Autowired
    private CaseService caseService;

    @Autowired
    private OfficerAssignmentRepository assignmentRepository;

    @ModelAttribute
    public void addAttributes(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);
        }
    }

    private boolean isNotOfficer(HttpSession session) {
        User user = (User) session.getAttribute("user");
        return user == null || (user.getRole() != Role.POLICE_OFFICER && user.getRole() != Role.DETECTIVE);
    }

    // Get the police stations the officer is assigned to
    private List<PoliceStation> getOfficerStations(Long officerId) {
        List<OfficerAssignment> assignments = assignmentRepository.findByUserId(officerId);
        return assignments.stream().map(OfficerAssignment::getPoliceStation).distinct().toList();
    }

    @GetMapping("/complaints")
    public String viewComplaints(HttpSession session, Model model) {
        if (isNotOfficer(session)) return "redirect:/dashboard";
        
        User officer = (User) session.getAttribute("user");
        List<PoliceStation> stations = getOfficerStations(officer.getId());
        
        List<Complaint> allComplaints = new ArrayList<>();
        for (PoliceStation station : stations) {
            allComplaints.addAll(complaintService.getComplaintsByStation(station.getId()));
        }
        
        model.addAttribute("complaints", allComplaints);
        return "officer/complaints";
    }

    @PostMapping("/complaints/{id}/fir")
    public String fileFIR(@PathVariable Long id, @RequestParam String firDetails, HttpSession session) {
        if (isNotOfficer(session)) return "redirect:/dashboard";
        
        caseService.fileFIR(id, firDetails);
        return "redirect:/officer/case-registry?success=FIR Filed and Case Opened";
    }
    
    @PostMapping("/complaints/{id}/reject")
    public String rejectComplaint(@PathVariable Long id, HttpSession session) {
        if (isNotOfficer(session)) return "redirect:/dashboard";
        
        caseService.rejectComplaint(id);
        return "redirect:/officer/complaints?success=Complaint Rejected";
    }

    @GetMapping("/case-registry")
    public String viewCaseRegistry(HttpSession session, Model model) {
        if (isNotOfficer(session)) return "redirect:/dashboard";
        
        User officer = (User) session.getAttribute("user");
        List<PoliceStation> stations = getOfficerStations(officer.getId());
        
        List<CaseFile> allCases = new ArrayList<>();
        // In a real app, optimize this with a custom repository query
        for (CaseFile caseFile : caseService.getAllCases()) {
            if (stations.contains(caseFile.getComplaint().getPoliceStation())) {
                allCases.add(caseFile);
            }
        }
        
        model.addAttribute("cases", allCases);
        return "officer/case-registry";
    }

    @GetMapping("/cases/{id}")
    public String viewCaseDetails(@PathVariable Long id, HttpSession session, Model model) {
        if (isNotOfficer(session)) return "redirect:/dashboard";
        
        CaseFile caseFile = caseService.getCaseFileById(id);
        if (caseFile == null) return "redirect:/officer/case-registry";
        
        model.addAttribute("caseFile", caseFile);
        return "officer/update-case";
    }

    @PostMapping("/cases/{id}/update")
    public String updateCaseDetails(@PathVariable Long id, @ModelAttribute CaseFile updates, HttpSession session) {
        if (isNotOfficer(session)) return "redirect:/dashboard";
        
        caseService.updateCaseDetails(id, updates);
        return "redirect:/officer/cases/" + id + "?success=Case Updated Successfully";
    }
}
