package com.pd.auth_service.client;

import com.pd.auth_service.domain.dto.KeycloakTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

@FeignClient(name = "keycloak-client",url = "http://localhost:9090/realms/TradeHub/protocol/openid-connect")
public interface KeycloakClient {

    @PostMapping(value = "/token",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    KeycloakTokenResponse getAccessToken(Map<String,?> formParams);
}
