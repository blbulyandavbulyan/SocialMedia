package com.blbulyandavbulyan.socialmedia.controllers;

import com.blbulyandavbulyan.socialmedia.dtos.AuthorizationRequest;
import com.blbulyandavbulyan.socialmedia.dtos.JwtTokenResponse;
import com.blbulyandavbulyan.socialmedia.dtos.RegistrationRequest;
import com.blbulyandavbulyan.socialmedia.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/")
public class AuthController {
    private AuthService authService;
    @PostMapping("/register")
    public void registerUser(@Valid @RequestBody RegistrationRequest registrationRequest){
        authService.registerUser(registrationRequest.username(), registrationRequest.password(), registrationRequest.email());
    }
    @PostMapping("/login")
    public JwtTokenResponse login(@Valid @RequestBody AuthorizationRequest authorizationRequest){
        return new JwtTokenResponse(authService.authorize(authorizationRequest.username(), authorizationRequest.password()));
    }
}
