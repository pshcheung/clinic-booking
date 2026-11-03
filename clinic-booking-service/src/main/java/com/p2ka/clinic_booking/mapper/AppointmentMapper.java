package com.p2ka.clinic_booking.mapper;

import com.p2ka.clinic_booking.dto.AppointmentDto;
import com.p2ka.clinic_booking.model.Appointment;
import com.p2ka.clinic_booking.model.Therapist;
import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AppointmentMapper {
    @Mapping(target = "id", source = "id", qualifiedByName = "objectIdToString")
    @Mapping(target = "startDateTime", source = "startDateTime")
    @Mapping(target = "endDateTime", source = "endDateTime")
    @Mapping(target = "assignedTherapistId", source = "assignedTherapist", qualifiedByName = "therapistToId")
    @Mapping(target = "assignedTherapistName", source = "assignedTherapist", qualifiedByName = "therapistToName")
    @Mapping(target = "comments", source = "comments")
    @Mapping(target = "createdDateTime", source = "createdDateTime")
    @Mapping(target = "status", source = "status", qualifiedByName = "appointmentStatusToString")
    AppointmentDto toDto(Appointment appointment);

    @Mapping(target = "id", source = "id", qualifiedByName = "stringToObjectId")
    @Mapping(target = "startDateTime", source = "startDateTime")
    @Mapping(target = "endDateTime", source = "endDateTime")
    @Mapping(target = "assignedTherapist", ignore = true)
    @Mapping(target = "comments", source = "comments")
    @Mapping(target = "status", source = "status", qualifiedByName = "stringToAppointmentStatus")
    @Mapping(target = "createdDateTime", ignore = true)
    @Mapping(target = "lastUpdatedDateTime", ignore = true)
    @Mapping(target = "lastUpdatedBy", ignore = true)
    Appointment toModel(AppointmentDto timeSlotDto);

    @Named("objectIdToString")
    default String objectIdToString(ObjectId id) {
        return id == null ? null : id.toHexString();
    }

    @Named("stringToObjectId")
    default ObjectId stringToObjectId(String id) {
        return id == null || id.isBlank() ? null : new ObjectId(id);
    }

    @Named("therapistToId")
    default String therapistToId(Therapist therapist) {
        return (therapist == null || therapist.getId() == null) ? null : therapist.getId().toHexString();
    }

    @Named("therapistToName")
    default String therapistToName(Therapist therapist) {
        return therapist == null ? null : String.join(" ",
                therapist.getFirstName() == null ? "" : therapist.getFirstName(),
                therapist.getLastName() == null ? "" : therapist.getLastName()).trim();
    }

    @Named("appointmentStatusToString")
    default String appointmentStatusToString(Appointment.AppointmentStatus type) {
        return type == null ? null : type.name();
    }

    @Named("stringToAppointmentStatus")
    default Appointment.AppointmentStatus stringToAppointmentStatus(String type) {
        return type == null || type.isBlank() ? null : Appointment.AppointmentStatus.valueOf(type);
    }
}
