package com.p2ka.clinic_booking.service;

import com.p2ka.clinic_booking.dto.TimeslotDto;
import com.p2ka.clinic_booking.dto.LookupPeriodParameter;
import com.p2ka.clinic_booking.mapper.TimeslotMapper;
import com.p2ka.clinic_booking.mapper.TherapistMapper;
import com.p2ka.clinic_booking.model.Therapist;
import com.p2ka.clinic_booking.model.Timeslot;
import com.p2ka.clinic_booking.model.Appointment;
import com.p2ka.clinic_booking.repository.BookingRepository;
import com.p2ka.clinic_booking.repository.TherapistRepository;
import com.p2ka.clinic_booking.repository.TimeslotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class TherapistService {
    @Autowired
    private TherapistRepository therapistRepository;
    @Autowired
    private TimeslotRepository timeSlotRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private TherapistMapper therapistMapper; // Autowired seamlessly due to componentModel
    @Autowired
    private TimeslotMapper timeslotMapper;
    @Autowired
    private AppointmentService appointmentService;

    public List<TimeslotDto> getTimeslots(Jwt jwtToken, LookupPeriodParameter period) {
        List<Timeslot> timeslots = period == null || period.startDate() == null || period.endDate() == null
                ? timeSlotRepository.findByTherapistSubject(jwtToken.getSubject())
                : timeSlotRepository.findInPeriodForTherapist(
                        jwtToken.getSubject(), periodStart(period), periodEndExclusive(period));
        return timeslots.stream()
                .map(timeslotMapper::toDto)
                .toList();
    }

    public List<Appointment> getAppointments(Jwt jwtToken, LookupPeriodParameter period) {
        if (period != null && period.startDate() != null && period.endDate() != null) {
            return appointmentService.getAppointments(jwtToken, periodStart(period), periodEndExclusive(period));
        }
        Therapist therapist = getTherapist(jwtToken);
        String firstName = therapist.getFirstName() == null ? "" : therapist.getFirstName().trim();
        String lastName = therapist.getLastName() == null ? "" : therapist.getLastName().trim();
        String therapistName = (firstName + " " + lastName).trim();
        List<String> names = new ArrayList<>();
        addName(names, therapistName);
        addName(names, "Dr. " + therapistName);
        addName(names, jwtToken.getClaimAsString("name"));
        addName(names, jwtToken.getClaimAsString("preferred_username"));
        addName(names, therapist.getEmailAddress());
        if (names.isEmpty()) return List.of();
        return appointmentService.findPatientByDoctorNames(names);
    }

    private LocalDateTime periodStart(LookupPeriodParameter period) {
        return LocalDate.parse(period.startDate(), DateTimeFormatter.BASIC_ISO_DATE).atStartOfDay();
    }

    private LocalDateTime periodEndExclusive(LookupPeriodParameter period) {
        return LocalDate.parse(period.endDate(), DateTimeFormatter.BASIC_ISO_DATE).plusDays(1).atStartOfDay();
    }

    private void addName(List<String> names, String value) {
        if (value != null && !value.isBlank() && !names.contains(value.trim())) {
            names.add(value.trim());
        }
    }

    public void saveTimeslots(List<TimeslotDto> timeslots, Jwt jwtToken) {
        Therapist therapist = this.getTherapist(jwtToken);
        therapist.setSubject(jwtToken.getSubject());
        therapist = therapistRepository.save(therapist);
        List<Timeslot> slots = timeslots.stream().map(timeslotMapper::toModel).toList();
        for (Timeslot slot : slots) {
            slot.setTherapist(therapist);
            slot.setStatus(Timeslot.TimeslotStatus.PROPOSED);
        }
        timeSlotRepository.saveAll(slots);
    }

    public Timeslot updatePendingTimeslot(String id, Timeslot changes, Jwt jwtToken) {
        ObjectId objectId;
        try {
            objectId = new ObjectId(id);
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Timeslot not found");
        }
        Timeslot existing = timeSlotRepository.findByIdAndTherapistSubject(objectId, jwtToken.getSubject())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Timeslot not found"));
        if (existing.getStatus() != Timeslot.TimeslotStatus.PROPOSED) {
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

/*    public List<Timeslot> getSlotDetails(String email) {
        return timeSlotRepository.findByEmail(email);
    }

    public List<Timeslot> getSlotList() {
        return timeSlotRepository.findAll();
    }

    public List<Timeslot> getSlotDetailsWithUniqueDoctors() {
        return timeSlotRepository.findAll();
    }

    public List<Timeslot> getSlotDetailsWithUniqueSpecializations() {
        return timeSlotRepository.findAll();
    }

    public Appointment addNewAppointment(Appointment appointment) {
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

    public List<Appointment> findPatientByEmail(String email) {
        return appointmentsRepository.findByEmail(email);
    }

    public List<Appointment> findPatientBySlot(String slot) {
        return appointmentsRepository.findBySlot(slot);
    }

    public List<Appointment> findPatientByDoctorName(String doctorname) {
        return appointmentsRepository.findByDoctorname(doctorname);
    }

    public List<Appointment> getAllPatients() {
        return appointmentsRepository.findAll();
    }

    public void updatePatientId(String patientID, String doctorname, String patientname, String date) {
        appointmentsRepository.UpdatePatientid(patientID, doctorname, patientname, date);
    }*/
}
