package com.p2ka.clinic_booking.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AppointmentDto (
    String id,
    LocalDateTime startDateTime,
    LocalDateTime endDateTime,
    String assignedTherapistId,
    String assignedTherapistName,
    String comments,
    LocalDateTime createdDateTime,
    String status) {
}
