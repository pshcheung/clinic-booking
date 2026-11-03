package com.p2ka.clinic_booking.controller;

import com.p2ka.clinic_booking.model.*;
import com.p2ka.clinic_booking.service.AppointmentBookingService;
import com.p2ka.clinic_booking.service.DoctorRegistrationService;
import com.p2ka.clinic_booking.service.PrescriptionService;
import com.p2ka.clinic_booking.service.TherapistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/therapists")
public class TherapistController {
    @Autowired
    private DoctorRegistrationService doctorRegisterService;

    @Autowired
    private AppointmentBookingService appointmentBookingService;

    @Autowired
    private TherapistService therapistService;

/*    @GetMapping("/")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<List<Therapist>> getDoctors() throws Exception {
        List<Therapist> therapists = doctorRegisterService.getAllDoctors();
        return new ResponseEntity<List<Therapist>>(therapists, HttpStatus.OK);
    }

    @GetMapping("/counts")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<List<Integer>> getTotalDoctors() throws Exception {
        List<Therapist> therapists = doctorRegisterService.getAllDoctors();
        List<Integer> al = new ArrayList<>();
        al.add(therapists.size());
        return new ResponseEntity<List<Integer>>(al, HttpStatus.OK);
    }

    @GetMapping("/gettotalslots")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<List<Integer>> getTotalSlots() throws Exception {
        List<TimeSlot> slots = appointmentBookingService.getSlotList();
        List<Integer> al = new ArrayList<>();
        al.add(slots.size());
        return new ResponseEntity<List<Integer>>(al, HttpStatus.OK);
    }

    @GetMapping("/acceptstatus/{email}")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<List<String>> updateStatus(@PathVariable String email) throws Exception {
        doctorRegisterService.updateStatus(email);
        List<String> al = new ArrayList<>();
        al.add("accepted");
        return new ResponseEntity<List<String>>(al, HttpStatus.OK);
    }

    @GetMapping("/rejectstatus/{email}")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<List<String>> rejectStatus(@PathVariable String email) throws Exception {
        doctorRegisterService.rejectStatus(email);
        List<String> al = new ArrayList<>();
        al.add("rejected");
        return new ResponseEntity<List<String>>(al, HttpStatus.OK);
    }

    @GetMapping("/acceptpatient/{slot}")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<List<String>> updatePatientStatus(@PathVariable String slot) throws Exception {
        List<Appointments> patient = appointmentBookingService.findPatientBySlot(slot);
        String doctorName = "";
        for (Appointments obj : patient) {
            if (obj.getSlot().equals(slot))
                doctorName = obj.getDoctorname();
        }
        doctorRegisterService.updatePatientStatus(slot, doctorName);
        List<String> al = new ArrayList<>();
        al.add("accepted");
        return new ResponseEntity<List<String>>(al, HttpStatus.OK);
    }

    @GetMapping("/rejectpatient/{slot}")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<List<String>> rejectPatientStatus(@PathVariable String slot) throws Exception {
        List<Appointments> patient = appointmentBookingService.findPatientBySlot(slot);
        String doctorName = "";
        for (Appointments obj : patient) {
            if (obj.getSlot().equals(slot))
                doctorName = obj.getDoctorname();
        }
        doctorRegisterService.rejectPatientStatus(slot, doctorName);
        List<String> al = new ArrayList<>();
        al.add("rejected");
        return new ResponseEntity<List<String>>(al, HttpStatus.OK);
    }*/

    @GetMapping("/timeslots")
    @CrossOrigin(origins = "http://localhost:4200")
    @PreAuthorize("hasAnyRole('ROLE_clinic_therapist_role', 'ROLE_clinic_manager_role', 'ROLE_clinic_admin_role')")
    public ResponseEntity<List<TimeSlot>> getTimeslots(@AuthenticationPrincipal Jwt jwtToken) throws Exception {
        return new ResponseEntity<>(therapistService.getTimeSlots(jwtToken), HttpStatus.OK);
    }

    @GetMapping("/appointments")
    @CrossOrigin(origins = "http://localhost:4200")
    @PreAuthorize("hasAnyRole('ROLE_clinic_therapist_role', 'ROLE_clinic_manager_role', 'ROLE_clinic_admin_role')")
    public ResponseEntity<List<Appointments>> getAppointments(@AuthenticationPrincipal Jwt jwtToken) {
        return ResponseEntity.ok(therapistService.getAppointments(jwtToken));
    }

    @PostMapping("/timeslots")
    @CrossOrigin(origins = "http://localhost:4200")
    @PreAuthorize("hasAnyRole('ROLE_clinic_therapist_role', 'ROLE_clinic_manager_role', 'ROLE_clinic_admin_role')")
    public String addTimeslots(@RequestBody List<TimeSlot> timeSlots,
                               @AuthenticationPrincipal Jwt jwtToken) throws Exception {
        therapistService.saveTimeSlots(timeSlots, jwtToken);
        return "modified successfully !!!";
    }

    @PutMapping("/timeslots/{id}")
    @CrossOrigin(origins = "http://localhost:4200")
    @PreAuthorize("hasAnyRole('ROLE_clinic_therapist_role', 'ROLE_clinic_manager_role', 'ROLE_clinic_admin_role')")
    public ResponseEntity<TimeSlot> updateTimeslot(@PathVariable String id,
                                                    @RequestBody TimeSlot changes,
                                                    @AuthenticationPrincipal Jwt jwtToken) {
        return ResponseEntity.ok(therapistService.updatePendingTimeSlot(id, changes, jwtToken));
    }

/*    @GetMapping("/doctorlistbyemail/{email}")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<List<Therapist>> getRequestHistoryByEmail(@PathVariable String email) throws Exception {
        System.out.print("requesting");
        List<Therapist> history = doctorRegisterService.getDoctorListByEmail(email);
        return new ResponseEntity<List<Therapist>>(history, HttpStatus.OK);
    }

    @GetMapping("/slotDetails/{email}")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<List<TimeSlot>> getSlotDetails(@PathVariable String email) throws Exception {
        List<TimeSlot> slots = appointmentBookingService.getSlotDetails(email);
        return new ResponseEntity<List<TimeSlot>>(slots, HttpStatus.OK);
    }

    @GetMapping("/slotDetails")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<List<TimeSlot>> getSlotList() throws Exception {
        List<TimeSlot> slots = appointmentBookingService.getSlotList();
        return new ResponseEntity<List<TimeSlot>>(slots, HttpStatus.OK);
    }

    @GetMapping("/slotDetailsWithUniqueDoctors")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<Set<String>> getSlotDetailsWithUniqueDoctors() throws Exception {
        List<TimeSlot> slots = appointmentBookingService.getSlotDetailsWithUniqueDoctors();
        Set<String> set = new LinkedHashSet<>();
        for (TimeSlot obj : slots) {
            set.add(obj.getDoctorname());
        }
        return new ResponseEntity<Set<String>>(set, HttpStatus.OK);
    }

    @GetMapping("/slotDetailsWithUniqueSpecializations")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<Set<String>> getSlotDetailsWithUniqueSpecializations() throws Exception {
        List<TimeSlot> slots = appointmentBookingService.getSlotDetailsWithUniqueSpecializations();
        Set<String> set = new LinkedHashSet<>();
        for (TimeSlot obj : slots) {
            set.add(obj.getSpecialization());
        }
        return new ResponseEntity<Set<String>>(set, HttpStatus.OK);
    }

    @GetMapping("/patientlistbydoctoremail/{email}")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<List<Appointments>> getPatientDetails(@PathVariable String email) throws Exception {
        List<Therapist> history = doctorRegisterService.getDoctorListByEmail(email);
        String doctorname = "";
        for (Therapist obj : history) {
            if (obj.getEmail().equals(email)) {
                doctorname = obj.getDoctorname();
                break;
            }
        }
        List<Appointments> patients = appointmentBookingService.findPatientByDoctorName(doctorname);
        return new ResponseEntity<List<Appointments>>(patients, HttpStatus.OK);
    }

    @PostMapping("/addPrescription")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<Prescription> addNewPrescription(@RequestBody Prescription prescription) throws Exception {
        List<Appointments> patients = appointmentBookingService.getAllPatients();
        String patientID = "";
        for (Appointments obj : patients) {
            if (obj.getPatientname().equals(prescription.getPatientname())) {
                patientID = obj.getPatientid();
                break;
            }
        }
        prescription.setPatientid(patientID);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String todayDate = formatter.format(date);
        prescription.setDate(todayDate);

        Prescription prescriptions = prescriptionService.savePrescriptions(prescription);
        return new ResponseEntity<Prescription>(prescriptions, HttpStatus.OK);
    }

    @GetMapping("/doctorProfileDetails/{email}")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<List<Therapist>> getDoctorProfileDetails(@PathVariable String email) throws Exception {
        List<Therapist> therapists = doctorRegisterService.fetchProfileByEmail(email);
        return new ResponseEntity<List<Therapist>>(therapists, HttpStatus.OK);
    }

    @PutMapping("/updatedoctor")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<Therapist> updateDoctorProfile(@RequestBody Therapist therapist) throws Exception {
        Therapist doctorobj = doctorRegisterService.updateDoctorProfile(therapist);
        return new ResponseEntity<Therapist>(doctorobj, HttpStatus.OK);
    }

    @GetMapping("/patientlistbydoctoremailanddate/{email}")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<List<Appointments>> getPatientDetailsAndDate(@PathVariable String email) throws Exception {
        List<Appointments> patients = appointmentBookingService.getAllPatients();
        List<Therapist> history = doctorRegisterService.getDoctorListByEmail(email);
        String doctorname = "";
        for (Therapist obj : history) {
            if (obj.getEmail().equals(email)) {
                doctorname = obj.getDoctorname();
                break;
            }
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String todayDate = formatter.format(date);
        List<Appointments> appointmentsList = new ArrayList<>();
        for (Appointments obj : patients) {
            if (obj.getDoctorname().equals(doctorname) && obj.getDate().equals(todayDate)) {
                doctorname = obj.getDoctorname();
                appointmentsList.add(obj);
                break;
            }
        }
        return new ResponseEntity<List<Appointments>>(appointmentsList, HttpStatus.OK);
    }*/
}
