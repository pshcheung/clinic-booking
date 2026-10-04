package com.p2ka.clinic_booking.repository;

import com.p2ka.clinic_booking.model.Therapist;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TherapistRepository extends MongoRepository<Therapist, ObjectId> {
    Optional<Therapist> findBySubject(String subject);
    //    void saveAll(List<TimeSlot> timeSlots);

//    List<TimeSlot> findByEmail(String email);
}