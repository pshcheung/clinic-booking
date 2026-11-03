package com.p2ka.clinic_booking.controller;

import com.p2ka.clinic_booking.model.AuthRequest;
import com.p2ka.clinic_booking.model.Therapist;
import com.p2ka.clinic_booking.model.User;
import com.p2ka.clinic_booking.service.DoctorRegistrationService;
import com.p2ka.clinic_booking.service.UserRegistrationService;
import com.p2ka.clinic_booking.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LoginController {

    @Autowired
    private UserRegistrationService userRegisterService;

    @Autowired
    private JwtUtils jwtUtil;

/*    @Autowired
    private AuthenticationManager authenticationManager;*/

    @Autowired
    private DoctorRegistrationService doctorRegisterService;

    @GetMapping("/")
    public String welcomeMessage() {
        return "Welcome to Clinic Booking application !!!";
    }

    @PostMapping("/authenticate")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<String> generateToken(@RequestBody AuthRequest authRequest) throws Exception {
        try {
            System.out.println(authRequest.getEmail());
            System.out.println(authRequest.getPassword());
            List<User> users = userRegisterService.getAllUsers();
            String currentEmail = "";
            for (User obj : users) {
                if (obj.getEmail().equalsIgnoreCase(authRequest.getEmail())) {
                    currentEmail = obj.getEmail();
                }
            }
            // authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(currentEmail, authRequest.getPassword()));
        } catch (Exception ex) {
            throw new Exception("Invalid Username/password");
        }
        return new ResponseEntity<String>(jwtUtil.generateToken(authRequest.getEmail()), HttpStatus.OK);
    }

    @PostMapping("/loginuser")
    @CrossOrigin(origins = "http://localhost:4200")
    public User loginUser(@RequestBody User user) throws Exception {
        String currEmail = user.getEmail();
        String currPassword = null;

        User userObj = null;
        if (currEmail != null && currPassword != null) {
            userObj = userRegisterService.fetchUserByEmailAndPassword(currEmail, currPassword);
        }
        if (userObj == null) {
            throw new Exception("User does not exists!!! Please enter valid credentials...");
        }
        return userObj;
    }

    @PostMapping("/logindoctor")
    @CrossOrigin(origins = "http://localhost:4200")
    public Therapist loginDoctor(@RequestBody Therapist therapist) throws Exception {
        String currEmail = therapist.getEmail();
        String currPassword = null;

        Therapist therapistObj = null;
        if (currEmail != null && currPassword != null) {
            therapistObj = doctorRegisterService.fetchDoctorByEmailAndPassword(currEmail, currPassword);
        }
        if (therapistObj == null) {
            throw new Exception("User does not exists!!! Please enter valid credentials...");
        }
        return therapistObj;
    }

}
