package com.p2ka.clinic_booking.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Services")
public class ServiceType extends BaseEntity {
    private String code;
    private String name;
    private String description;
    private int minimumDuration;
    private String clientRateCode;
    private String therapistRateCode;
}