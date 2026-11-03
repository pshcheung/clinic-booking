package com.p2ka.clinic_booking.repository;

import com.p2ka.clinic_booking.model.Appointment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BookingRepository extends MongoRepository<Appointment, Integer> {
/*    List<Appointment> findByEmail(String email);

    List<Appointment> findBySlot(String slot);

    List<Appointment> findByDoctorname(String doctorname);*/

/*    void updateAmstatus(String doctorname, String date);

    void updateNoonstatus(String doctorname, String date);

    void updatePmstatus(String doctorname, String date);

    void UpdatePatientid(String patientID, String doctorname, String patientname, String date);*/

}