package com.p2ka.clinic_booking.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.p2ka.clinic_booking.model.Prescription;

public interface PrescriptionRepository extends CrudRepository<Prescription, Integer>
{

	List<Prescription> findByPatientname(String patientname);

}