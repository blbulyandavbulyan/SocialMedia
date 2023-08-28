package com.blbulyandavbulyan.socialmedia.controllers;

import com.blbulyandavbulyan.socialmedia.dtos.security.AuthorizationRequest;
import com.blbulyandavbulyan.socialmedia.dtos.security.JwtTokenResponse;
import com.blbulyandavbulyan.socialmedia.dtos.security.RegistrationRequest;
import com.blbulyandavbulyan.socialmedia.services.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/")
@AllArgsConstructor
public class AuthController {
    private AuthService authService;
    @PostMapping("/register")
    public void registerUser(@Validated @RequestBody RegistrationRequest registrationRequest){
        authService.registerUser(registrationRequest.username(), registrationRequest.password(), registrationRequest.email());
    }
    @PostMapping("/login")
    public JwtTokenResponse login(@Validated @RequestBody AuthorizationRequest authorizationRequest){
        return new JwtTokenResponse(authService.authorize(authorizationRequest.username(), authorizationRequest.password()));
    }
}
