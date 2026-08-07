package com.pd.auth_service.controller;

import com.pd.auth_service.domain.dto.*;
import com.pd.auth_service.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> createUser(@Valid @RequestBody SignupRequest signupRequest){
        SignupResponse response = authService.signupUser(signupRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@Valid @RequestBody LoginRequest loginRequest){
        LoginResponse response = authService.loginUser(loginRequest);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refreshUser(@RequestBody RefreshRequest refreshRequest){
        LoginResponse loginResponse = authService.refreshUser(refreshRequest);
        return new ResponseEntity<>(loginResponse,HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logoutUser(@RequestBody LogoutRequest logoutRequest){
        authService.logoutUser(logoutRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/keycloak/login")
    public void keycloakLogin(HttpServletResponse response) throws IOException{
        String redirectUri = "http://localhost:8080/api/v1/auth/keycloak/callback";
        String url = "http://localhost:9090/realms/TradeHub/protocol/openid-connect/auth"
                + "?client_id=tradehub-client"
                + "&response_type=code"
                + "&scope=openid"
                + "&redirect_uri=" + redirectUri;
        response.sendRedirect(url);
    }

    @GetMapping("/keycloak/callback")
    public ResponseEntity<LoginResponse> keycloakCallback(@RequestParam String code){
        LoginResponse response = authService.keycloakLogin(code);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PostMapping("/protected")
    public ResponseEntity<String> protectedCheck(){
        return ResponseEntity.ok("Protected");
    }

    @PatchMapping("/password")
    public ResponseEntity<Void> changePassword(@RequestBody ChangePasswordRequest request){
        authService.changePassword(request);
        return ResponseEntity.ok().build();
    }

}
