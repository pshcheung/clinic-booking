package com.p2ka.clinic_booking.service;

import com.p2ka.clinic_booking.dto.AppointmentDto;
import com.p2ka.clinic_booking.dto.TimeslotDto;
import com.p2ka.clinic_booking.mapper.AppointmentMapper;
import com.p2ka.clinic_booking.mapper.TimeslotMapper;
import com.p2ka.clinic_booking.model.Appointment;
import com.p2ka.clinic_booking.model.Timeslot;
import com.p2ka.clinic_booking.repository.AppointmentsRepository;
import com.p2ka.clinic_booking.repository.TimeslotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.time.LocalDateTime;

@Service
public class AppointmentService {
    @Autowired
    private TimeslotRepository timeSlotRepository;
    @Autowired
    private AppointmentsRepository appointmentsRepository;

    @Autowired
    private AppointmentMapper appointmentMapper;

    public List<AppointmentDto> getAppointments(Jwt jwtToken) {
        return appointmentsRepository.findByAssignedTherapist(jwtToken.getSubject()).stream()
                .map(appointmentMapper::toDto)
                .toList();
    }

    public List<Appointment> getAppointments(Jwt jwtToken, LocalDateTime start, LocalDateTime endExclusive) {
        return appointmentsRepository
                .findInPeriodForAssignedTherapist(
                        jwtToken.getSubject(), start, endExclusive);
    }

    public List<Appointment> findPatientByDoctorName(String doctorName) {
        // return appointmentsRepository.findByDoctorname(doctorName);
        return null;
    }

    public List<Appointment> findPatientByDoctorNames(List<String> doctorNames) {
        //        return appointmentsRepository.findByDoctornameIn(doctorNames);
        return new ArrayList<>();
    }

    public Appointment addNewAppointment(Appointment appointment) {
        return appointmentsRepository.save(appointment);
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
