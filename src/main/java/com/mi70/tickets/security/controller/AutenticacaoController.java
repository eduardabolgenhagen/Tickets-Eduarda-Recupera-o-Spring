package com.mi70.tickets.security.controller;

import com.mi70.tickets.model.dto.UsuarioDTO;
import com.mi70.tickets.security.model.entity.UserJpa;
import com.mi70.tickets.security.utils.CookieUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
@CrossOrigin
@AllArgsConstructor
public class AutenticacaoController {
    private AuthenticationManager authenticationManager;
    private final CookieUtils cookieUtils = new CookieUtils();

    @PostMapping
    public ResponseEntity<Object> autenticacao(@RequestBody UsuarioDTO usuarioDTO, HttpServletResponse response) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(usuarioDTO.getEmail(), usuarioDTO.getSenha());
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            if (authentication.isAuthenticated()) {
                UserJpa userJpa = (UserJpa) authentication.getPrincipal();
                Cookie jwtCookie = cookieUtils.gerarTokenCookie(userJpa);
                response.addCookie(jwtCookie);
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userJpa.getUsername(), userJpa.getPassword(), userJpa.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                return ResponseEntity.ok().build();
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    // Verificar se necessário LOGOUT
}
