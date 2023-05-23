package com.mi70.tickets.security.config;

import com.mi70.tickets.security.filter.AutenticacaoFiltro;
import com.mi70.tickets.security.service.JpaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class AutenticacaoConfig {
    @Autowired
    JpaService jpaService;

    @Autowired
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        // Utilizar criptografia
        authenticationManagerBuilder.userDetailsService(jpaService).passwordEncoder(NoOpPasswordEncoder.getInstance());
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("/**"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests()
                .requestMatchers(HttpMethod.POST, "/api/login", "/login").permitAll()

                // TICKET
                // SEM LOGIN
                .requestMatchers(HttpMethod.GET, "/api/ticket", "/ticket", "/ticket/*").permitAll()
                // COM LOGIN
                .requestMatchers(HttpMethod.POST, "/api/ticket", "/ticket")
                .hasAnyAuthority("USUARIO", "JUNIOR", "PLENO", "SENIOR", "ADMINISTRADOR")
                .requestMatchers(HttpMethod.PUT, "/ticket/*")
                .hasAnyAuthority("JUNIOR", "PLENO", "SENIOR", "ADMINISTRADOR")
                .requestMatchers(HttpMethod.DELETE, "/api/ticket", "/ticket")
                .hasAuthority("ADMINISTRADOR")

                // COMENTÁRIO
                .requestMatchers(HttpMethod.POST, "/api/comentario", "/comentario")
                .hasAnyAuthority("USUARIO", "JUNIOR", "PLENO", "SENIOR", "ADMINISTRADOR")
                .requestMatchers(HttpMethod.PUT, "/comentario/*")
                .hasAnyAuthority("JUNIOR", "PLENO", "SENIOR", "ADMINISTRADOR")
                .requestMatchers(HttpMethod.GET, "api/comentario", "/comentario", "/comentario/*")
                .hasAnyAuthority("USUARIO", "JUNIOR", "PLENO", "SENIOR", "ADMINISTRADOR")
                .requestMatchers(HttpMethod.DELETE, "/api/comentario", "/comentario")
                .hasAuthority("ADMINISTRADOR")

                // USUÁRIO
                .requestMatchers(HttpMethod.POST, "/api/usuario", "/usuario")
                .hasAnyAuthority("SENIOR", "ADMINISTRADOR")
                .requestMatchers(HttpMethod.PUT, "/usuario/*")
                .hasAnyAuthority("SENIOR", "ADMINISTRADOR")
                .requestMatchers(HttpMethod.GET, "/api/usuario", "/usuario", "/usuario/*")
                .hasAnyAuthority("USUARIO", "JUNIOR", "PLENO", "SENIOR", "ADMINISTRADOR")
                .requestMatchers(HttpMethod.DELETE, "/api/usuario", "/usuario")
                .hasAuthority("ADMINISTRADOR")

                // ENDEREÇO
                .requestMatchers(HttpMethod.POST, "/api/endereco", "/endereco")
                .hasAnyAuthority("PLENO", "SENIOR", "ADMINISTRADOR")
                .requestMatchers(HttpMethod.PUT, "/endereco/*")
                .hasAnyAuthority("PLENO", "SENIOR", "ADMINISTRADOR")
                .requestMatchers(HttpMethod.GET, "/api/endereco", "/endereco/", "/endereco/*")
                .hasAnyAuthority("USUARIO", "JUNIOR", "PLENO", "SENIOR", "ADMINISTRADOR")
                .requestMatchers(HttpMethod.DELETE, "/api/endereco", "/endereco")
                .hasAuthority("ADMINISTRADOR");

        httpSecurity.cors().configurationSource(corsConfigurationSource());
        httpSecurity.csrf().disable();
        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().addFilterBefore(new AutenticacaoFiltro(jpaService), UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
