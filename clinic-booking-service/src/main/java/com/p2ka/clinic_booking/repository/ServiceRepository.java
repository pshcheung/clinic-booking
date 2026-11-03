package com.p2ka.clinic_booking.repository;

import com.p2ka.clinic_booking.model.ServiceType;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ServiceRepository extends MongoRepository<ServiceType, String> {
}
