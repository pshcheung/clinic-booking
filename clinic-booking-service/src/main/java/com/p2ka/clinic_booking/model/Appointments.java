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
public class Appointments {
    private int id;
    private String patientid;
    private String patientname;
    private String email;
    private String doctorname;
    private String specialization;
    private String date;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String age;
    private String gender;
    private String problem;
    private String slot;
    private String appointmentstatus;
    private String admissionstatus;
}
