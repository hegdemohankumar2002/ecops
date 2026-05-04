package com.dsu.ecops.controller;

import com.dsu.ecops.model.*;
import com.dsu.ecops.service.CaseService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/magistrate")
public class MagistrateController {

    @Autowired
    private CaseService caseService;

    @ModelAttribute
    public void addAttributes(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);
        }
    }

    private boolean isNotMagistrate(HttpSession session) {
        User user = (User) session.getAttribute("user");
        return user == null || user.getRole() != Role.MAGISTRATE;
    }

    @GetMapping("/cases")
    public String reviewCases(HttpSession session, Model model) {
        if (isNotMagistrate(session)) return "redirect:/dashboard";
        
        model.addAttribute("cases", caseService.getAllCases());
        return "magistrate/review-cases";
    }

    @PostMapping("/cases/{id}/warrant")
    public String updateWarrantStatus(@PathVariable Long id, @RequestParam WarrantStatus status, HttpSession session) {
        if (isNotMagistrate(session)) return "redirect:/dashboard";
        
        caseService.updateWarrantStatus(id, status);
        return "redirect:/magistrate/cases?success=Warrant Status Updated";
    }
}
