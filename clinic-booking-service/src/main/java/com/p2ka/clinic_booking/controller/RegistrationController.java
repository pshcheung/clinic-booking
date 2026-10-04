package com.p2ka.clinic_booking.controller;

import com.p2ka.clinic_booking.model.Therapist;
import com.p2ka.clinic_booking.model.User;
import com.p2ka.clinic_booking.service.DoctorRegistrationService;
import com.p2ka.clinic_booking.service.UserRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RegistrationController {
    @Autowired
    private UserRegistrationService userRegisterService;

    @Autowired
    private DoctorRegistrationService doctorRegisterService;

    @PostMapping("/registeruser")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        String currEmail = user.getEmail();
        if (currEmail != null && !currEmail.trim().isEmpty()) {
            User existing = userRegisterService.fetchUserByEmail(currEmail);
            if (existing != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("User with email " + currEmail + " already exists");
            }
        }
        User saved = userRegisterService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PostMapping("/registerdoctor")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<?> registerDoctor(@RequestBody Therapist therapist) {
        String currEmail = therapist.getEmail();
        if (currEmail != null && !currEmail.trim().isEmpty()) {
            Therapist existing = doctorRegisterService.fetchDoctorByEmail(currEmail);
            if (existing != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Therapist with email " + currEmail + " already exists");
            }
        }
        Therapist saved = doctorRegisterService.saveDoctor(therapist);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PostMapping("/addDoctor")
    @CrossOrigin(origins = "http://localhost:4200")
    public Therapist addNewDoctor(@RequestBody Therapist therapist) throws Exception {
        Therapist therapistObj = null;
        therapistObj = doctorRegisterService.saveDoctor(therapist);
        return therapistObj;
    }

    @GetMapping("/gettotalusers")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<List<Integer>> getTotalSlots() throws Exception {
        List<User> users = userRegisterService.getAllUsers();
        List<Integer> al = new ArrayList<>();
        al.add(users.size());
        return new ResponseEntity<List<Integer>>(al, HttpStatus.OK);
    }

}
