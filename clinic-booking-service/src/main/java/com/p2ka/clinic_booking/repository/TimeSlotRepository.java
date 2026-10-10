package com.p2ka.clinic_booking.repository;

import com.p2ka.clinic_booking.model.TimeSlot;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface TimeSlotRepository extends MongoRepository<TimeSlot, ObjectId> {
    List<TimeSlot> findByTherapistSubject(String subject);
    Optional<TimeSlot> findByIdAndTherapistSubject(ObjectId id, String subject);
//    void saveAll(List<TimeSlot> timeSlots);

//    List<TimeSlot> findByEmail(String email);
}
