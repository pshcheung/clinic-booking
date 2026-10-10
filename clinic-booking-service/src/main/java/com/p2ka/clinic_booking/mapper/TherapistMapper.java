package com.p2ka.clinic_booking.mapper;

import com.p2ka.clinic_booking.model.Therapist;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.springframework.security.oauth2.jwt.Jwt;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TherapistMapper {
    // Map JWT to Therapist
    @Mapping(target = "subject", expression = "java(getSubject(jwtToken))")
    @Mapping(target = "email", expression = "java(getEmail(jwtToken))")
    @Mapping(target = "firstName", expression = "java(getGivenName(jwtToken))")
    @Mapping(target = "lastName", expression = "java(getFamilyName(jwtToken))")
    Therapist jwtToTherapist(Jwt jwtToken);

    default String getSubject(Jwt jwtToken) {
        return jwtToken.getSubject();
    }
    default String getEmail(Jwt jwtToken) {
        return jwtToken.getClaimAsString("email");
    }
    default String getGivenName(Jwt jwtToken) {
        return jwtToken.getClaimAsString("given_name");
    }
    default String getFamilyName(Jwt jwtToken) {
        return jwtToken.getClaimAsString("family_name");
    }

    // Map User to UserDTO
//    @Mapping(source = "fullName", target = "name") // Handles different field names
//    UserDTO userToUserDto(User user);

    // Map UserDTO back to User
//    @Mapping(source = "name", target = "fullName")
//    @Mapping(target = "id", ignore = true) // Ignores the database ID field when mapping back
//    User userDtoToUser(UserDTO userDto);
}
