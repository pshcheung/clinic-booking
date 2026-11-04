package com.p2ka.clinic_booking.mapper;

import com.p2ka.clinic_booking.dto.TimeslotDto;
import com.p2ka.clinic_booking.model.ServiceType;
import com.p2ka.clinic_booking.model.Timeslot;
import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import java.util.Arrays;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TimeslotMapper {
    @Mapping(target = "id", source = "id", qualifiedByName = "objectIdToString")
    @Mapping(target = "servicesToProvide", source = "servicesToProvide", qualifiedByName = "serviceTypesToCodes")
    @Mapping(target = "startDateTime", source = "startDateTime")
    @Mapping(target = "endDateTime", source = "endDateTime")
    @Mapping(target = "type", source = "type", qualifiedByName = "timeSlotTypeToString")
    @Mapping(target = "status", source = "status", qualifiedByName = "timeSlotStatusToString")
    TimeslotDto toDto(Timeslot timeSlot);

    @Mapping(target = "id", source = "id", qualifiedByName = "stringToObjectId")
    @Mapping(target = "servicesToProvide", source = "servicesToProvide", qualifiedByName = "codesToServiceTypes")
    @Mapping(target = "startDateTime", source = "startDateTime")
    @Mapping(target = "endDateTime", source = "endDateTime")
    @Mapping(target = "type", source = "type", qualifiedByName = "stringToTimeslotType")
    @Mapping(target = "status", source = "status", qualifiedByName = "stringToTimeslotStatus")
    @Mapping(target = "therapist", ignore = true)
    @Mapping(target = "createdDateTime", ignore = true)
    @Mapping(target = "lastUpdatedDateTime", ignore = true)
    @Mapping(target = "lastUpdatedBy", ignore = true)
    Timeslot toModel(TimeslotDto timeSlotDto);

    @Named("objectIdToString")
    default String objectIdToString(ObjectId id) {
        return id == null ? null : id.toHexString();
    }

    @Named("stringToObjectId")
    default ObjectId stringToObjectId(String id) {
        return id == null || id.isBlank() ? null : new ObjectId(id);
    }

    @Named("serviceTypesToCodes")
    default String[] serviceTypesToCodes(List<ServiceType> serviceTypes) {
        return serviceTypes == null ? null : serviceTypes.stream()
                .map(ServiceType::getCode)
                .toArray(String[]::new);
    }

    @Named("codesToServiceTypes")
    default List<ServiceType> codesToServiceTypes(String[] codes) {
        return codes == null ? null : Arrays.stream(codes)
                .map(code -> {
                    ServiceType serviceType = new ServiceType();
                    serviceType.setCode(code);
                    return serviceType;
                })
                .toList();
    }

    @Named("timeSlotTypeToString")
    default String timeSlotTypeToString(Timeslot.TimeslotType type) {
        return type == null ? null : type.name();
    }

    @Named("stringToTimeslotType")
    default Timeslot.TimeslotType stringToTimeslotType(String type) {
        return type == null || type.isBlank() ? null : Timeslot.TimeslotType.valueOf(type);
    }

    @Named("timeSlotStatusToString")
    default String timeSlotStatusToString(Timeslot.TimeslotStatus status) {
        return status == null ? null : status.name();
    }

    @Named("stringToTimeslotStatus")
    default Timeslot.TimeslotStatus stringToTimeslotStatus(String status) {
        return status == null || status.isBlank() ? null : Timeslot.TimeslotStatus.valueOf(status);
    }
}
