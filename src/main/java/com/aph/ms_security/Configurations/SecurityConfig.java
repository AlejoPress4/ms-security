package com.aph.ms_security.Configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/public/**", "/login", "/oauth2/**").permitAll()
                        .anyRequest().authenticated())
                .oauth2Login(oauth2 -> oauth2
                        .defaultSuccessUrl("/oauth2/user", true) // Redirige al endpoint después de un login exitoso
                        .failureUrl("/login?error=true") // Maneja errores de autenticación
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login").permitAll());

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return (request, response, authentication) -> {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            // Obtener los datos del usuario autenticado
            var principal = authentication.getPrincipal();
            if (principal instanceof org.springframework.security.oauth2.core.user.OAuth2User) {
                var oauth2User = (org.springframework.security.oauth2.core.user.OAuth2User) principal;
                var userAttributes = oauth2User.getAttributes();

                // Enviar los datos del usuario como respuesta JSON
                response.getWriter()
                        .write(new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(userAttributes));
            } else {
                response.getWriter().write(
                        "{\"message\": \"Inicio de sesión exitoso, pero no se pudieron obtener los datos del usuario.\"}");
            }
        };
    }
}