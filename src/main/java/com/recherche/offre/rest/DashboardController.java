package com.recherche.offre.rest;

import com.recherche.offre.dto.DashboardDto;
import com.recherche.offre.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@RestController
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/user/{userId}")
    public DashboardDto getDashboard(@PathVariable final Long userId) {
        return dashboardService.getDashboard(userId);
    }

}
