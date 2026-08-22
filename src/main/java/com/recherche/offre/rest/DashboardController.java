package com.recherche.offre.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/dashboard")
public class DashboardController {

    @GetMapping("/")
    public String getDashboard() {
        return "Dashboard information";
    }

}
