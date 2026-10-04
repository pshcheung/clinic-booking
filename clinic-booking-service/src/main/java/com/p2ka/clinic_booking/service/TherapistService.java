package com.p2ka.clinic_booking.service;

import com.p2ka.clinic_booking.mapper.TherapistMapper;
import com.p2ka.clinic_booking.model.Therapist;
import com.p2ka.clinic_booking.model.TimeSlot;
import com.p2ka.clinic_booking.repository.BookingRepository;
import com.p2ka.clinic_booking.repository.TherapistRepository;
import com.p2ka.clinic_booking.repository.TimeSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TherapistService {
    @Autowired
    private TherapistRepository therapistRepository;
    @Autowired
    private TimeSlotRepository timeSlotRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private TherapistMapper therapistMapper; // Autowired seamlessly due to componentModel

    public List<TimeSlot> getTimeSlots(Jwt jwtToken) {
        Therapist therapist = this.getTherapist(jwtToken);
        return therapist.getTimeSlots();
    }

    public void saveTimeSlots(List<TimeSlot> timSlots) {
        timeSlotRepository.saveAll(timSlots);
    }

    private Therapist getTherapist(Jwt jwtToken) {
        Optional<Therapist> existingTherapist = therapistRepository.findBySubject(jwtToken.getClaimAsString("subject"));
        return existingTherapist.orElseGet(() -> therapistMapper.jwtToTherapist(jwtToken));
    }

/*    public List<TimeSlot> getSlotDetails(String email) {
        return timeSlotRepository.findByEmail(email);
    }

    public List<TimeSlot> getSlotList() {
        return timeSlotRepository.findAll();
    }

    public List<TimeSlot> getSlotDetailsWithUniqueDoctors() {
        return timeSlotRepository.findAll();
    }

    public List<TimeSlot> getSlotDetailsWithUniqueSpecializations() {
        return timeSlotRepository.findAll();
    }

    public Appointments addNewAppointment(Appointments appointment) {
        return appointmentsRepository.save(appointment);
    }

    public int bookAMSlot(String doctorname, String date) {
        appointmentsRepository.updateAmstatus(doctorname, date);
        return 1;
    }

    public int bookNoonSlot(String doctorname, String date) {
        appointmentsRepository.updateNoonstatus(doctorname, date);
        return 1;
    }

    public int bookPMSlot(String doctorname, String date) {
        appointmentsRepository.updatePmstatus(doctorname, date);
        return 1;
    }

    public List<Appointments> findPatientByEmail(String email) {
        return appointmentsRepository.findByEmail(email);
    }

    public List<Appointments> findPatientBySlot(String slot) {
        return appointmentsRepository.findBySlot(slot);
    }

    public List<Appointments> findPatientByDoctorName(String doctorname) {
        return appointmentsRepository.findByDoctorname(doctorname);
    }

    public List<Appointments> getAllPatients() {
        return appointmentsRepository.findAll();
    }

    public void updatePatientId(String patientID, String doctorname, String patientname, String date) {
        appointmentsRepository.UpdatePatientid(patientID, doctorname, patientname, date);
    }*/
}
