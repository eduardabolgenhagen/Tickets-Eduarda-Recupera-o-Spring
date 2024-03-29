package com.mi70.tickets.security.utils;

import com.mi70.tickets.security.model.entity.UserJpa;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtUtils {
    private final String senhaForte = "c127a7b6adb013a5ff879ae71afa62afa4b4ceb72afaa54711dbcde67b6dc325";

    public String gerarToken(UserJpa userJpa) {
        return Jwts.builder()
                .setIssuer("Tickets")
                .setSubject(userJpa.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + 60000))
                .signWith(SignatureAlgorithm.HS256, senhaForte)
                .compact();
    }

    public void validarToken(String token) {
        try {
            Jwts.parser().setSigningKey(senhaForte).parseClaimsJws(token);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(senhaForte).parseClaimsJws(token).getBody().getSubject();
    }
}
