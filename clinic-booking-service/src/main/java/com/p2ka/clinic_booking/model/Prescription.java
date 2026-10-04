package com.p2ka.clinic_booking.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Prescription")
public class Prescription {
    private int id;
    private String patientid;
    private String patientname;
    private String doctorname;
    private String disease;
    private String gender;
    private String age;
    private String date;
    private String prescription;
    private String admissionstatus;
}
