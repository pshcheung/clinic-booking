package com.p2ka.clinic_booking.repository;

import com.p2ka.clinic_booking.model.Therapist;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DoctorRegistrationRepository extends MongoRepository<Therapist, String> {
    Therapist findByEmail(String email);

    List<Therapist> findDoctorListByEmail(String email);

//    Therapist findByDoctorname(String doctorname);
//
//    Therapist findByEmailAndPassword(String email, String password);

    List<Therapist> findProfileByEmail(String email);

/*    void updateStatus(String email);

    void rejectStatus(String email);

    void updatePatientStatus(String slot, String doctorname);

    void rejectPatientStatus(String slot, String doctorname);*/
}