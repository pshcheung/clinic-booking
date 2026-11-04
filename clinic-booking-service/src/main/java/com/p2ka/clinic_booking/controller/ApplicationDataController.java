package com.p2ka.clinic_booking.controller;

import com.p2ka.clinic_booking.dto.ApplicationData;
import com.p2ka.clinic_booking.service.ApplicationDataService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public/application-data")
public class ApplicationDataController {
    private final ApplicationDataService applicationDataService;

    public ApplicationDataController(ApplicationDataService applicationDataService) {
        this.applicationDataService = applicationDataService;
    }

    @GetMapping
    public ApplicationData getApplicationData() {
        return applicationDataService.getApplicationData();
    }
}
