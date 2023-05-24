package com.mi70.tickets.security.filter;

import com.mi70.tickets.security.service.JpaService;
import com.mi70.tickets.security.utils.CookieUtils;
import com.mi70.tickets.security.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@AllArgsConstructor
public class AutenticacaoFiltro extends OncePerRequestFilter {
    private final CookieUtils cookieUtils = new CookieUtils();
    private final JwtUtils jwtUtils = new JwtUtils();
    private JpaService jpaService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = cookieUtils.getTokenCookie(request);
            jwtUtils.validarToken(token);

//            if (token == null && validarUrl(request.getRequestURI(), request.getMethod()) == true) {
//                System.out.println("Deixa fazer a requisição");
//            }

            UserDetails user = jpaService.loadUserByUsername(jwtUtils.getUsername(token));

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        } catch (Exception e) {
            System.out.println("Primeiro catch");
            try {
                // Validar url
                validarUrl(request.getRequestURI(), request.getMethod());
            } catch (Exception ex) {
                System.out.println("Segundo catch");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        } finally {
            filterChain.doFilter(request, response);
        }
    }

    private void validarUrl(String url, String metod) {
        if (!(url.equals("/api/login")
                || url.equals("/login")
                || url.equals("/api/login/auth")
                || url.equals("/login/auth")
                || url.equals("/api/ticket")
                || url.equals("/ticket"))) {
            throw new RuntimeException();
        } else {
            System.out.println("URL validada.");
        }
    }
}
