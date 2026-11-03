package com.p2ka.clinic_booking.config;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "application")
public record ApplicationConfig (
    @NotBlank String title,
    @NotBlank String version
){}
