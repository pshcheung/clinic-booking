package com.p2ka.clinic_booking.service;

import com.p2ka.clinic_booking.model.Therapist;
import com.p2ka.clinic_booking.repository.DoctorRegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorRegistrationService {
    @Autowired
    private DoctorRegistrationRepository doctorRegistrationRepo;

    public Therapist saveDoctor(Therapist therapist) {
        return doctorRegistrationRepo.save(therapist);
    }

    public Therapist updateDoctorProfile(Therapist therapist) {
        return doctorRegistrationRepo.save(therapist);
    }

    public List<Therapist> getAllDoctors() {
        return doctorRegistrationRepo.findAll();
    }

    public void updateStatus(String email) {
//        doctorRegistrationRepo.updateStatus(email);
    }

    public void rejectStatus(String email) {
//        doctorRegistrationRepo.rejectStatus(email);
        System.out.print("rejected");
    }

    public void updatePatientStatus(String slot, String doctorname) {
//        doctorRegistrationRepo.updatePatientStatus(slot, doctorname);
    }

    public void rejectPatientStatus(String slot, String doctorname) {
//        doctorRegistrationRepo.rejectPatientStatus(slot, doctorname);
        System.out.print("rejected");
    }

    public List<Therapist> getDoctorListByEmail(String email) {
        return doctorRegistrationRepo.findDoctorListByEmailAddress(email);
    }

    public Therapist fetchDoctorByEmail(String email) {
        return doctorRegistrationRepo.findByEmailAddress(email);
    }

    public Therapist fetchDoctorByDoctorname(String doctorname) {
//        return doctorRegistrationRepo.findByDoctorname(doctorname);
        return null;
    }

    public Therapist fetchDoctorByEmailAndPassword(String email, String password) {
//        return doctorRegistrationRepo.findByEmailAndPassword(email, password);
        return null;
    }

    public List<Therapist> fetchProfileByEmail(String email) {
        return doctorRegistrationRepo.findProfileByEmailAddress(email);
    }

}