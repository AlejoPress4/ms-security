package com.aph.ms_security.Controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthController {

    private final OAuth2AuthorizedClientService authorizedClientService;

    public AuthController(OAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
    }

    @GetMapping("/oauth2/user")
    public Map<String, Object> getUserDetails(@AuthenticationPrincipal OAuth2User principal) {
        Map<String, Object> response = new HashMap<>(principal.getAttributes());

        // Obtener el access_token del OAuth2AuthorizedClient
        OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient(
                "google", // El nombre del registro del cliente (debe coincidir con el configurado en
                          // application.properties)
                principal.getName());

        if (authorizedClient != null && authorizedClient.getAccessToken() != null) {
            response.put("access_token", authorizedClient.getAccessToken().getTokenValue());
        } else {
            response.put("access_token", "No access token available");
        }

        return response; // Devuelve los datos del usuario junto con el access_token
    }
}