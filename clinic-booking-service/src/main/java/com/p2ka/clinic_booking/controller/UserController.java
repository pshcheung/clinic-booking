package com.p2ka.clinic_booking.controller;

import com.p2ka.clinic_booking.model.*;
import com.p2ka.clinic_booking.service.AppointmentBookingService;
import com.p2ka.clinic_booking.service.PrescriptionService;
import com.p2ka.clinic_booking.service.UserRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RestController
public class UserController {
    @Autowired
    private UserRegistrationService userRegisterService;

    @Autowired
    private AppointmentBookingService appointmentBookingService;

    @Autowired
    private PrescriptionService prescriptionService;

    @GetMapping("/userlist")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<List<User>> getUsers() throws Exception {
        List<User> users = userRegisterService.getAllUsers();
        return new ResponseEntity<List<User>>(users, HttpStatus.OK);
    }

    @GetMapping("/getprescriptionbyname/{patientname}")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<List<Prescription>> getPrescriptionByPatientname(@PathVariable String patientname) throws Exception {
        List<Prescription> prescription = prescriptionService.getPrescriptionByPatientname(patientname);
        return new ResponseEntity<List<Prescription>>(prescription, HttpStatus.OK);
    }

    @GetMapping("/patientlistbyemail/{email}")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<List<Appointments>> getPatientList(@PathVariable String email) throws Exception {
//        List<Appointments> patients = appointmentBookingService.findPatientByEmail(email);
        List<Appointments> patients = null;
        return new ResponseEntity<List<Appointments>>(patients, HttpStatus.OK);
    }

    @GetMapping("/patientlist")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<List<Appointments>> getPatients() throws Exception {
//        List<Appointments> patients = appointmentBookingService.getAllPatients();
        List<Appointments> patients = null;
        return new ResponseEntity<List<Appointments>>(patients, HttpStatus.OK);
    }

    @GetMapping("/gettotalpatients")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<List<Integer>> getTotalPatients() throws Exception {
//        List<Appointments> patients = appointmentBookingService.getAllPatients();
        List<Appointments> patients = null;
        List<Integer> al = new ArrayList<>();
        al.add(patients.size());
        return new ResponseEntity<List<Integer>>(al, HttpStatus.OK);
    }

    @GetMapping("/gettotalappointments")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<List<Integer>> getTotalAppointments() throws Exception {
//        List<Appointments> patients = appointmentBookingService.getAllPatients();
        List<Appointments> patients = null;
        List<Integer> al = new ArrayList<>();
        al.add(patients.size());
        return new ResponseEntity<List<Integer>>(al, HttpStatus.OK);
    }

    @GetMapping("/gettotalprescriptions")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<List<Integer>> getTotalPrescriptions() throws Exception {
        List<Prescription> patients = prescriptionService.getAllPrescriptions();
        List<Integer> al = new ArrayList<>();
        al.add(patients.size());
        return new ResponseEntity<List<Integer>>(al, HttpStatus.OK);
    }

    @GetMapping("/profileDetails/{email}")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<List<User>> getProfileDetails(@PathVariable String email) throws Exception {
        List<User> users = userRegisterService.fetchProfileByEmail(email);
        return new ResponseEntity<List<User>>(users, HttpStatus.OK);
    }

    @PutMapping("/updateuser")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<User> updateUserProfile(@RequestBody User user) throws Exception {
        User userobj = userRegisterService.updateUserProfile(user);
        return new ResponseEntity<User>(userobj, HttpStatus.OK);
    }

    @PostMapping("/bookNewAppointment")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<Appointments> addNewAppointment(@RequestBody Appointments appointment) {
        if (appointment.getPatientname() == null || appointment.getPatientname().isBlank()
                || appointment.getDoctorname() == null || appointment.getDoctorname().isBlank()
                || ((appointment.getDate() == null || appointment.getDate().isBlank())
                && appointment.getStartDateTime() == null)) {
            return ResponseEntity.badRequest().build();
        }
        if (appointment.getStartDateTime() != null && appointment.getEndDateTime() != null
                && !appointment.getEndDateTime().isAfter(appointment.getStartDateTime())) {
            return ResponseEntity.badRequest().build();
        }
        if ((appointment.getDate() == null || appointment.getDate().isBlank())
                && appointment.getStartDateTime() != null) {
            appointment.setDate(appointment.getStartDateTime().toLocalDate().toString());
        }
        if (appointment.getId() == 0) {
            appointment.setId(ThreadLocalRandom.current().nextInt(1, Integer.MAX_VALUE));
        }
        if (appointment.getPatientid() == null || appointment.getPatientid().isBlank()) {
            appointment.setPatientid(getPatientID());
        }
        if (appointment.getAppointmentstatus() == null || appointment.getAppointmentstatus().isBlank()
                || "false".equalsIgnoreCase(appointment.getAppointmentstatus())) {
            appointment.setAppointmentstatus("PENDING");
        }
        Appointments saved = appointmentBookingService.addNewAppointment(appointment);
        return new ResponseEntity<>(saved, HttpStatus.OK);
    }

    public String getPatientID() {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            int index = (int) (AlphaNumericString.length() * Math.random());
            sb.append(AlphaNumericString.charAt(index));
        }
        return sb.toString();
    }

}
