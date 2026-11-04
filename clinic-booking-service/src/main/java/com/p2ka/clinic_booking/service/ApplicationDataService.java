package com.p2ka.clinic_booking.service;

import com.p2ka.clinic_booking.config.ApplicationConfig;
import com.p2ka.clinic_booking.dto.ApplicationData;
import com.p2ka.clinic_booking.dto.ServiceTypeDto;
import com.p2ka.clinic_booking.repository.ServiceRepository;
import com.p2ka.clinic_booking.model.ServiceType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationDataService {
    private final ApplicationConfig applicationConfig;
    private final ServiceRepository serviceRepository;

    private final List<ServiceType> serviceTypes = List.of(
            new ServiceType("BUCCAL_MASSAGE", "Buccal Massage", "Perform Buccal Massage", 15, "CR1", "TR1"),
            new ServiceType("LYMPHATIC_DRAINAGE", "Lymphatic Massage", "Perform Lymphatic Massage", 15, "CR2", "TR2"),
            new ServiceType("SPORTS_MASSAGE", "Sports Massage", "Perform Sports Massage", 30, "CR3", "TR3"),
            new ServiceType("SPECIAL_MASSAGE", "Special Massage", "Perform Special Massage", 60, "CR4", "TR4"),
            new ServiceType("THAI_MASSAGE", "Thai Massage", "Perform Thai Massage", 15, "CR5", "TR5")
    );

    public ApplicationDataService(ApplicationConfig applicationConfig, ServiceRepository serviceRepository) {
        this.applicationConfig = applicationConfig;
        this.serviceRepository = serviceRepository;
    }

    public ApplicationData getApplicationData() {
//        var availableServices = serviceRepository.findAll().stream()
        var availableServices = this.serviceTypes.stream()
                .map(service -> new ServiceTypeDto(null, service.getCode(), service.getName(), service.getDescription(),
                        service.getClientRateCode(), service.getTherapistRateCode()))
                .toList();
        return new ApplicationData(applicationConfig.title(), applicationConfig.version(),
                applicationConfig.organizationCode(), availableServices);
    }
}
