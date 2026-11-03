package com.p2ka.clinic_booking.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "users")
public class User {
    private String subject;  // unique id from the issuer.
    private String email;
    private String firstName;
    private String lastName;
    private String mobile;
    private String gender;
    private String age;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String province;
    private String status;
}
