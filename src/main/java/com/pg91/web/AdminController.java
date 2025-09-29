package com.pg91.web;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

public class AdminController {
    @GetMapping("/dashboard")
    public String viewDashboard(Model model) {
        return  "adminDashboard/dashboard";
    }

}
