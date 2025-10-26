package com.pg91.insurance.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {
    @GetMapping("/admin/dashboard")
    public String viewDashboard(Model model) {
        return  "adminDashboard/dashboard";
    }

}