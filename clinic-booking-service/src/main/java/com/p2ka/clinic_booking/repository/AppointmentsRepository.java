package com.p2ka.clinic_booking.repository;

import com.p2ka.clinic_booking.model.Appointments;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AppointmentsRepository extends MongoRepository<Appointments, Integer> {
    List<Appointments> findByEmail(String email);

    List<Appointments> findBySlot(String slot);

    List<Appointments> findByDoctorname(String doctorname);
    List<Appointments> findByDoctornameIn(List<String> doctorNames);

/*    void updateAmstatus(String doctorname, String date);

    void updateNoonstatus(String doctorname, String date);

    void updatePmstatus(String doctorname, String date);

    void UpdatePatientid(String patientID, String doctorname, String patientname, String date);*/

}
