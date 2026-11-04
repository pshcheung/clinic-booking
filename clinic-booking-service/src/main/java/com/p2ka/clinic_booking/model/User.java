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
public class User extends BaseEntity {
    private String subject;  // unique id from the issuer.
    private String emailAddress;
    private String firstName;
    private String lastName;
    private String contactPhoneNumber;
    private String gender;
    private String age;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String province;
    private String status;
}
