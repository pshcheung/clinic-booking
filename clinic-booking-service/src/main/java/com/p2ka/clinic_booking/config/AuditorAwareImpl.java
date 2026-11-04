package com.p2ka.clinic_booking.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class AuditorAwareImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assert authentication != null;
        if (authentication.getPrincipal() instanceof Jwt jwt) {
            return Optional.of(jwt.getClaim("sub")); // or jwt.getSubject()
        }
        return Optional.empty();
    }
}

