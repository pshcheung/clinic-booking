package com.p2ka.clinic_booking.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "therapists")
public class Therapist extends User {
    private int yearsOfExperience;
    private String description;
    private byte[] photo;
    private List<ServiceType> specialization;
    private List<Timeslot> timeslots;
    private List<Appointment> appointments;
}