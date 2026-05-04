package com.dsu.ecops.controller;

import com.dsu.ecops.model.*;
import com.dsu.ecops.service.ForumService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/officer/forum")
public class ForumController {

    @Autowired
    private ForumService forumService;

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
    public String viewForum(HttpSession session, Model model) {
        if (isNotAuthorized(session)) return "redirect:/dashboard";
        
        model.addAttribute("posts", forumService.getAllPosts());
        model.addAttribute("newPost", new ForumPost());
        return "officer/forum";
    }

    @PostMapping("/new")
    public String createPost(@ModelAttribute ForumPost post, HttpSession session) {
        if (isNotAuthorized(session)) return "redirect:/dashboard";
        
        User author = (User) session.getAttribute("user");
        forumService.createPost(author.getId(), post);
        return "redirect:/officer/forum?success=Post Created Successfully";
    }

    @GetMapping("/{id}")
    public String viewThread(@PathVariable Long id, HttpSession session, Model model) {
        if (isNotAuthorized(session)) return "redirect:/dashboard";
        
        ForumPost post = forumService.getPostById(id);
        if (post == null) return "redirect:/officer/forum";
        
        model.addAttribute("post", post);
        return "officer/forum-thread";
    }

    @PostMapping("/{id}/reply")
    public String addReply(@PathVariable Long id, @RequestParam String content, HttpSession session) {
        if (isNotAuthorized(session)) return "redirect:/dashboard";
        
        User author = (User) session.getAttribute("user");
        forumService.addReply(id, author.getId(), content);
        return "redirect:/officer/forum/" + id + "?success=Reply Added";
    }
}
