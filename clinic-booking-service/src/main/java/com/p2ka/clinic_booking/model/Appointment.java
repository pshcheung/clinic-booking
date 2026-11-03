package com.p2ka.clinic_booking.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "appointments")
public class Appointment extends BaseEntity {
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Therapist assignedTherapist;
    private String comments;
    private AppointmentStatus status;

    public enum AppointmentStatus {
        REQUESTED("RQT"),
        ACCEPTED("ACP"),
        REJECTED("RJT"),
        CANCELLED("CNL"),
        UNATTENDED("UAT"),
        COMPETED("CMP");

        AppointmentStatus(String type) {
        }
    }
}
