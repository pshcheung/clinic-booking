package com.p2ka.clinic_booking.dto;

import java.util.List;

public record ApplicationData(String title, String version, String organizationCode,
                              List<ServiceTypeDto> availableServices) {}
