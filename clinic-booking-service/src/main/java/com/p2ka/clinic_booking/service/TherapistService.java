package com.p2ka.clinic_booking.service;

import com.p2ka.clinic_booking.mapper.TherapistMapper;
import com.p2ka.clinic_booking.model.Therapist;
import com.p2ka.clinic_booking.model.TimeSlot;
import com.p2ka.clinic_booking.model.Appointments;
import com.p2ka.clinic_booking.repository.BookingRepository;
import com.p2ka.clinic_booking.repository.TherapistRepository;
import com.p2ka.clinic_booking.repository.TimeSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

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
    @Autowired
    private AppointmentBookingService appointmentBookingService;

    public List<TimeSlot> getTimeSlots(Jwt jwtToken) {
        return timeSlotRepository.findByTherapistSubject(jwtToken.getSubject());
    }

    public List<Appointments> getAppointments(Jwt jwtToken) {
        Therapist therapist = getTherapist(jwtToken);
        String firstName = therapist.getFirstName() == null ? "" : therapist.getFirstName().trim();
        String lastName = therapist.getLastName() == null ? "" : therapist.getLastName().trim();
        String therapistName = (firstName + " " + lastName).trim();
        List<String> names = new ArrayList<>();
        addName(names, therapistName);
        addName(names, "Dr. " + therapistName);
        addName(names, jwtToken.getClaimAsString("name"));
        addName(names, jwtToken.getClaimAsString("preferred_username"));
        addName(names, therapist.getEmail());
        if (names.isEmpty()) return List.of();
        return appointmentBookingService.findPatientByDoctorNames(names);
    }

    private void addName(List<String> names, String value) {
        if (value != null && !value.isBlank() && !names.contains(value.trim())) {
            names.add(value.trim());
        }
    }

    public void saveTimeSlots(List<TimeSlot> timSlots, Jwt jwtToken) {
        Therapist therapist = this.getTherapist(jwtToken);
        therapist.setSubject(jwtToken.getSubject());
        therapist = therapistRepository.save(therapist);
        for (TimeSlot slot : timSlots) {
            slot.setTherapist(therapist);
            slot.setStatus(TimeSlot.TimeSlotStatus.PROPOSED);
        }
        timeSlotRepository.saveAll(timSlots);
    }

    public TimeSlot updatePendingTimeSlot(String id, TimeSlot changes, Jwt jwtToken) {
        ObjectId objectId;
        try {
            objectId = new ObjectId(id);
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Timeslot not found");
        }
        TimeSlot existing = timeSlotRepository.findByIdAndTherapistSubject(objectId, jwtToken.getSubject())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Timeslot not found"));
        if (existing.getStatus() != TimeSlot.TimeSlotStatus.PROPOSED) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Only pending timeslots can be updated");
        }
        if (changes.getStartDateTime() == null || changes.getEndDateTime() == null ||
                !changes.getEndDateTime().isAfter(changes.getStartDateTime())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "End time must be after start time");
        }
        existing.setStartDateTime(changes.getStartDateTime());
        existing.setEndDateTime(changes.getEndDateTime());
        existing.setServicesToProvide(changes.getServicesToProvide());
        return timeSlotRepository.save(existing);
    }

    private Therapist getTherapist(Jwt jwtToken) {
        Optional<Therapist> existingTherapist = therapistRepository.findBySubject(jwtToken.getSubject());
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
