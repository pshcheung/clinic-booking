package com.p2ka.clinic_booking.dto;

import lombok.*;

@Builder
public record ServiceTypeDto(
    String id,
    String code,
    String name,
    String description,
    String clientRateCode,
    String therapistRateCode) {
}