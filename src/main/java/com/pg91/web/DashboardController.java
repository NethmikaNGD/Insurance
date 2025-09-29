package com.pg91.web;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

public class DashboardController {

    @GetMapping("/dashboard")
    public String viewDashboard() {
        return  "adminDashboard/dashboard";
    }
}
