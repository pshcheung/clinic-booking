package com.p2ka.clinic_booking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class ClinicBookingApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(ClinicBookingApiApplication.class, args);
        System.out.println("Project Running");
    }
}
