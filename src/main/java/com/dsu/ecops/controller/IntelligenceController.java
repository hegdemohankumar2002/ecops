package com.dsu.ecops.controller;

import com.dsu.ecops.model.*;
import com.dsu.ecops.service.CaseService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/officer/intelligence")
public class IntelligenceController {

    @Autowired
    private CaseService caseService;

    @ModelAttribute
    public void addAttributes(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);
        }
    }

    private boolean isNotAuthorized(HttpSession session) {
        User user = (User) session.getAttribute("user");
        return user == null || (user.getRole() != Role.POLICE_OFFICER && user.getRole() != Role.DETECTIVE);
    }

    @GetMapping
    public String searchIntelligence(@RequestParam(required = false) String q, HttpSession session, Model model) {
        if (isNotAuthorized(session)) return "redirect:/dashboard";
        
        if (q != null && !q.trim().isEmpty()) {
            List<CaseFile> results = caseService.searchIntelligence(q);
            model.addAttribute("results", results);
            model.addAttribute("query", q);
        }
        
        return "officer/intelligence-search";
    }
}
