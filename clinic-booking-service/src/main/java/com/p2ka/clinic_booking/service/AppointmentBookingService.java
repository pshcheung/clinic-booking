package com.p2ka.clinic_booking.service;

import com.p2ka.clinic_booking.model.Appointments;
import com.p2ka.clinic_booking.model.TimeSlot;
import com.p2ka.clinic_booking.repository.AppointmentsRepository;
import com.p2ka.clinic_booking.repository.TimeSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class AppointmentBookingService {
    @Autowired
    private TimeSlotRepository timeSlotRepository;

    @Autowired
    private AppointmentsRepository appointmentsRepository;

    public void saveSlots(List<TimeSlot> timSlots) {
        Random random = new Random();
        int val = random.nextInt(1000);
        timeSlotRepository.saveAll(timSlots);
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
