package com.p2ka.clinic_booking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.web.bind.annotation.BindParam;

public record LookupPeriodParameter(
//    @NotBlank(message = "Start-date cannot be empty")
    @Pattern(
            regexp = "^\\d{4}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])$",
//            regexp = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$",
            message = "Date must be in the format YYYYMMDD"
    )
    @BindParam("sd")
    String startDate,

    @Pattern(
            regexp = "^\\d{4}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])$",
            message = "Date must be in the format YYYYMMDD"
    )
    @BindParam("ed")
    String endDate
) {}
