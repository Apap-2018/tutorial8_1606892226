package com.apap.tutorial6.controller;

import com.apap.tutorial6.service.UserRoleService;
import com.apap.tutorial6.model.UserRoleModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {
    @Autowired
    private UserRoleService userService;

    @RequestMapping("/")
    public String home(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserRoleModel user = userService.findByUsername(authentication.getName());
        model.addAttribute("role", user.getRole().toLowerCase());
        return "home";
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }
}