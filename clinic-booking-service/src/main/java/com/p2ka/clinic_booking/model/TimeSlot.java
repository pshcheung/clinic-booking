package com.p2ka.clinic_booking.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "timeslots")
public class TimeSlot extends BaseEntity{
    Therapist therapist;
    List<ServiceType> servicesToProvide;
    LocalDateTime startDateTime;
    LocalDateTime endDateTime;
    TimeSlotType type;
    TimeSlotStatus status;

    public enum TimeSlotType {
        MIN_15_MINUTES("MIN_15"),
        MIN_30_MINUTES("MIN_30"),
        MIN_60_MINUTES("MIN_60");

        TimeSlotType(String type) {
        }
    }

    public enum TimeSlotStatus {
        PROPOSED("PRP"),
        ACCEPTED("ACP"),
        REJECTED("RJT");

        TimeSlotStatus(String type) {
        }
    }
}