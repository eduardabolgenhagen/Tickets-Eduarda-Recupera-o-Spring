package com.mi70.tickets.security.utils;

import com.mi70.tickets.security.model.entity.UserJpa;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.util.WebUtils;

public class CookieUtils {
    private final JwtUtils jwtUtils = new JwtUtils();

    public Cookie gerarTokenCookie(UserJpa userJpa) {
        String token = jwtUtils.gerarToken(userJpa);
        Cookie cookie = new Cookie("token", token);
        cookie.setPath("/");
        cookie.setMaxAge(60000);

        return cookie;
    }

    public String getTokenCookie(HttpServletRequest request) {
        try {
            Cookie cookie = WebUtils.getCookie(request, "token");
            return cookie.getValue();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
