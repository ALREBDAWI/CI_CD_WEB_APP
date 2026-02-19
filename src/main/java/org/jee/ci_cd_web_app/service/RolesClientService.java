package org.jee.ci_cd_web_app.service;

import org.jee.ci_cd_web_app.dto.RolesDto;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.List;
@Service
public class RolesClientService {
    private final WebClient webClient;

    public RolesClientService(WebClient webClient) {
        this.webClient = webClient;
    }

    public List<RolesDto> fetchRoles() {
        try {
            return webClient.get()
                    .uri("/roles")
                    .retrieve()
                    .bodyToFlux(RolesDto.class)
                    .collectList()
                    .block();
        } catch (Exception e) {
            System.out.println("Error fetching roles: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}
