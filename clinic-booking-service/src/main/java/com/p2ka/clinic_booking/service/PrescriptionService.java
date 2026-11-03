package com.p2ka.clinic_booking.service;

import com.p2ka.clinic_booking.model.Prescription;
import com.p2ka.clinic_booking.repository.PrescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrescriptionService {
    @Autowired
    private PrescriptionRepository prescriptionRepo;

    public Prescription savePrescriptions(Prescription prescription) {
        return prescriptionRepo.save(prescription);
    }

    public List<Prescription> getPrescriptionByPatientname(String patientname) {
        return prescriptionRepo.findByPatientname(patientname);
    }

    public List<Prescription> getAllPrescriptions() {
        return (List<Prescription>) prescriptionRepo.findAll();
    }
}
