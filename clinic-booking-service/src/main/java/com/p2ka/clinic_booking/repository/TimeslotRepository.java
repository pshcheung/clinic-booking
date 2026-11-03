package com.p2ka.clinic_booking.repository;

import com.p2ka.clinic_booking.model.Timeslot;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

public interface TimeslotRepository extends MongoRepository<Timeslot, ObjectId> {
    List<Timeslot> findByTherapistSubject(String subject);
    @Query("{ 'therapist.subject': ?0, 'startDateTime': { '$gte': ?1, '$lt': ?2 } }")
    List<Timeslot> findInPeriodForTherapist(
            String subject, LocalDateTime start, LocalDateTime endExclusive);
    Optional<Timeslot> findByIdAndTherapistSubject(ObjectId id, String subject);
//    void saveAll(List<Timeslot> timeslots);

//    List<Timeslot> findByEmail(String email);
}
