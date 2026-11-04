package com.p2ka.clinic_booking.repository;

import com.p2ka.clinic_booking.model.Appointment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.time.LocalDateTime;

public interface AppointmentsRepository extends MongoRepository<Appointment, Integer> {
//    List<Appointment> findByEmailAddress(String email);

//    List<Appointment> findBySlot(String slot);

/*    List<Appointment> findByDoctorname(String doctorname);
    List<Appointment> findByDoctornameIn(List<String> doctorNames);*/

    List<Appointment> findByAssignedTherapist(String subject);
    @Query("{ 'assignedTherapist.subject': ?0, 'startDateTime': { '$gte': ?1, '$lt': ?2 } }")
    List<Appointment> findInPeriodForAssignedTherapist(
            String subject, LocalDateTime start, LocalDateTime endExclusive);

/*    void updateAmstatus(String doctorname, String date);

    void updateNoonstatus(String doctorname, String date);

    void updatePmstatus(String doctorname, String date);

    void UpdatePatientid(String patientID, String doctorname, String patientname, String date);*/

}
