package com.example.springOAuth.security;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    @Value("${app.jwt.secret}")
    private String SECRET_KEY;

    @Value("${app.jwt.expiration.minutes}")
    private Long jwtExpirationMinutes;

    public static final String TOKEN_TYPE = "JWT";
    public static final String TOKEN_ISSUER = "springoauth-api";
    public static final String TOKEN_AUDIENCE = "springoauth-app";

    private Claims jwtExtractAllClaims(String token) {
        return Jwts.parser().verifyWith(getKeySignKey()).build().parseSignedClaims(token).getPayload();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {

        Claims claims = jwtExtractAllClaims(token);
        return claimResolver.apply(claims);

    }

    private SecretKey getKeySignKey() {

        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);

        return Keys.hmacShaKeyFor(keyBytes);

    }

    public boolean validateToken(String jwt) {
        try {

            jwtExtractAllClaims(jwt);
            return true;
        } catch (SignatureException ex) {
            System.out.println("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            System.out.println("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            System.out.println("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            System.out.println("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            System.out.println("JWT claims string is empty.");
        }

        return false;

    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);

    }

    public String generateToken(Map<String, Object> extraClaim, UserDetails userDetails) {
        return Jwts.builder()
                .claims(extraClaim)
                .subject(userDetails.getUsername())
                .expiration(Date.from(ZonedDateTime.now().plusMinutes(jwtExpirationMinutes).toInstant()))
                .issuedAt(Date.from(ZonedDateTime.now().toInstant()))
                .issuer(TOKEN_ISSUER)
                .audience().add(TOKEN_AUDIENCE)
                .and()
                .signWith(getKeySignKey(), Jwts.SIG.HS256)
                .compact();

    }

}
