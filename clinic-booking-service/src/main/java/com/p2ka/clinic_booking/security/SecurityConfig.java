package com.p2ka.clinic_booking.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity()
public class SecurityConfig {
    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuerUri;
    @Value("${application.jwt.audience}")
    private String expectedAudience;

    // private final JwtFilter jwtAuthFilter;
    // private final AuthenticationProvider authenticationProvider;

/*    @Bean
    // this is for signed JWT token
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req.requestMatchers(
                                        "/auth/**",
                                        "/v2/api-docs",
                                        "/v3/api-docs",
                                        "/v3/api-docs/**",
                                        "/swagger-resources",
                                        "/swagger-resources/**",
                                        "/configuration/ui",
                                        "/configuration/security",
                                        "/swagger-ui/**",
                                        "/webjars/**",
                                        "/swagger-ui.html"
                                )
                                .permitAll()
                                .anyRequest()
                                .authenticated()
                )
                .oauth2ResourceServer(auth ->
                        auth.jwt(token -> token.jwtAuthenticationConverter(new KeycloakJwtAuthenticationConverter())));


        return http.build();
    }
}*/

/*
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests.anyRequest().permitAll())
                .build();
    }
}*/

/*    private final JwtAuthConverter jwtAuthConverter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Disabled because JWTs are stateless
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/public/**").permitAll() // Public access
//                        .requestMatchers("/api/management/**").hasAuthority("ROLE_MANAGER") // Declarative access mapping
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter)) // Plugs in our Keycloak converter
                );

        return http.build();
    }*/

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disabled for REST APIs using stateless JWTs
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/public/**").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                );

        return http.build();
    }

    /**
     * Bypasses cryptographic verification to decode unsigned or unverified JWTs.
     */
/*    @Bean
    public JwtDecoder jwtDecoder() {
        // Creates a decoder that skips network calls to Keycloak's certs endpoint
        //return NimbusJwtDecoder.withJwkSetUri("http://localhost:8080/realms/clinic-booking/protocol/openid-connect/certs").build();

        *//*
         * Alternative: If Keycloak provides a pure plaintext/unsigned format
         * or if you want absolute pass-through without JWK validation requirements:
         *//*
         return jwtString -> {
             try {
                 com.nimbusds.jwt.JWT parsedJwt = com.nimbusds.jwt.JWTParser.parse(jwtString);
                 return new org.springframework.security.oauth2.jwt.Jwt(
                     jwtString,
                     parsedJwt.getJWTClaimsSet().getIssueTime().toInstant(),
                     parsedJwt.getJWTClaimsSet().getExpirationTime().toInstant(),
                     parsedJwt.getHeader().toJSONObject(),
                     parsedJwt.getJWTClaimsSet().getClaims()
                 );
             } catch (Exception e) {
                 throw new org.springframework.security.oauth2.jwt.JwtException("Invalid token format", e);
             }
         };
    }*/

    @Bean
    public JwtDecoder jwtDecoder() {
        // Automatically fetches JWKS URI from issuer-uri to validate signature and exp/nbf
        NimbusJwtDecoder jwtDecoder = (NimbusJwtDecoder) JwtDecoders.fromIssuerLocation(issuerUri);

        // Combine default issuer/timestamp validation with custom Audience validation
        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuerUri);
        OAuth2TokenValidator<Jwt> withAudience = new JwtAudienceValidator(expectedAudience);
        jwtDecoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(withIssuer, withAudience));

        return jwtDecoder;
    }

    // Custom Audience Validator
    static class JwtAudienceValidator implements OAuth2TokenValidator<Jwt> {
        private final String audience;

        JwtAudienceValidator(String audience) {
            this.audience = audience;
        }

        @Override
        public OAuth2TokenValidatorResult validate(Jwt jwt) {
            List<String> aud = jwt.getAudience();
            if (aud != null && aud.contains(audience)) {
                return OAuth2TokenValidatorResult.success();
            }
            return OAuth2TokenValidatorResult.failure(
                    new OAuth2Error("invalid_token", "The required audience is missing", null)
            );
        }
    }

    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new KeycloakRoleConverter());
        return converter;
    }
}