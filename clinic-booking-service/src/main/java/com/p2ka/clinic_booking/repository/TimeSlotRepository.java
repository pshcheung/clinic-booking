package com.p2ka.clinic_booking.repository;

import com.p2ka.clinic_booking.model.TimeSlot;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TimeSlotRepository extends MongoRepository<TimeSlot, Integer> {
//    void saveAll(List<TimeSlot> timeSlots);

//    List<TimeSlot> findByEmail(String email);
}