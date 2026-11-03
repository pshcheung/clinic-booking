package com.p2ka.clinic_booking.repository;

import com.p2ka.clinic_booking.model.Prescription;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PrescriptionRepository extends CrudRepository<Prescription, Integer> {
    List<Prescription> findByPatientname(String patientname);
}