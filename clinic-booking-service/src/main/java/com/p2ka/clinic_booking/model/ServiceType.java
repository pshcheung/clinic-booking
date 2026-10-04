package com.p2ka.clinic_booking.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "ServiceType")
public class ServiceType {
    private String code;
    private String name;
    private String description;
}