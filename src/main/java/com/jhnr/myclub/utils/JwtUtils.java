package com.jhnr.myclub.utils;

import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class JwtUtils {

    public static String generateAccessToken(String userName, String authorities, JwtEncoder jwtEncoder) {
        Instant instant = Instant.now();
        JwtClaimsSet jwtClaimsSetAccessToken = JwtClaimsSet.builder()
                .issuedAt(instant)
                .expiresAt(instant.plus(60, ChronoUnit.MINUTES))
                .subject(userName)
                .claim("scope", authorities)
                .build();

        JwtEncoderParameters jwtEncoderParametersAccessToken =
                JwtEncoderParameters.from(
                        JwsHeader.with(MacAlgorithm.HS512).build(),
                        jwtClaimsSetAccessToken
                );

        return jwtEncoder.encode(jwtEncoderParametersAccessToken).getTokenValue();
    }

    public static String generateRefreshToken(String userName, JwtEncoder jwtEncoder) {
        Instant instant = Instant.now();
        JwtClaimsSet jwtClaimsSetRefreshToken = JwtClaimsSet.builder()
                .issuedAt(instant)
                .expiresAt(instant.plus(365, ChronoUnit.DAYS))
                .subject(userName)
                .build();

        JwtEncoderParameters jwtEncoderParameterRefreshToken =
                JwtEncoderParameters.from(
                        JwsHeader.with(MacAlgorithm.HS512).build(),
                        jwtClaimsSetRefreshToken
                );

        return jwtEncoder.encode(jwtEncoderParameterRefreshToken).getTokenValue();

    }
}
