package com.blbulyandavbulyan.socialmedia.controllers;

import com.blbulyandavbulyan.socialmedia.dtos.AuthorizationRequest;
import com.blbulyandavbulyan.socialmedia.entites.User;
import com.blbulyandavbulyan.socialmedia.repositories.UserRepository;
import com.blbulyandavbulyan.socialmedia.services.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserRepository userRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @SpyBean
    private AuthService authService;

    @Test
    @DisplayName("normal login")
    public void normalLogin() throws Exception {
        AuthorizationRequest authorizationRequest = new AuthorizationRequest("david", "12345678632");
        User user = new User(authorizationRequest.username(), passwordEncoder.encode(authorizationRequest.password()), "test@gmail.com");
        Mockito.when(userRepository.findById(authorizationRequest.username())).thenReturn(Optional.of(user));
        mockMvc.perform(
                        post("/api/v1/login").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(authorizationRequest))
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(
                        document("normal-login", resourceDetails().description("Login request").tag("Registration and getting jwt token"),
                                responseFields(
                                        fieldWithPath("token").description("Your JWT token")
                                ),
                                requestFields(
                                        fieldWithPath("username").type(JsonFieldType.STRING).description("User name for login"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("Password password for login")
                                )
                        )
                );
        Mockito.verify(authService, Mockito.only()).authorize(authorizationRequest.username(), authorizationRequest.password());
    }

    @Test
    @DisplayName("login with not existing username")
    public void loginWhenUserWithThisNameNotFound() throws Exception {
        AuthorizationRequest authorizationRequest = new AuthorizationRequest("david", "12345678632");
        Mockito.when(userRepository.findById(authorizationRequest.username())).thenReturn(Optional.empty());
        mockMvc.perform(post("/api/v1/login").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(authorizationRequest)))
                .andExpect(status().isUnauthorized())
                .andDo(document("login-with-incorrect-username", resourceDetails().description("Login request").tag("Registration and getting jwt token"),
                                responseFields(
                                        fieldWithPath("httpStatusCode").type(JsonFieldType.STRING).description("The http status code(in words, not number)"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("The error message"),
                                        fieldWithPath("timestamp").type(JsonFieldType.STRING).description("The date and time of error happening")
                                )
                        )
                );
        Mockito.verify(authService, Mockito.only()).authorize(authorizationRequest.username(), authorizationRequest.password());
    }

    @Test
    @DisplayName("login with incorrect password")
    public void loginWithIncorrectPassword() throws Exception {
        AuthorizationRequest authorizationRequest = new AuthorizationRequest("david", "12345678632");
        User user = new User(authorizationRequest.username(), passwordEncoder.encode("dafaffwefa"), "test@gmail.com");
        Mockito.when(userRepository.findById(authorizationRequest.username())).thenReturn(Optional.of(user));
        mockMvc.perform(post("/api/v1/login").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(authorizationRequest)))
                .andExpect(status().isUnauthorized())
                .andDo(document("login-with-incorrect-password", resourceDetails().description("Login request").tag("Registration and getting jwt token"),
                                responseFields(
                                        fieldWithPath("httpStatusCode").type(JsonFieldType.STRING).description("The http status code(in words, not number)"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("The error message"),
                                        fieldWithPath("timestamp").type(JsonFieldType.STRING).description("The date and time of error happening")
                                )
                        )
                );
        Mockito.verify(authService, Mockito.only()).authorize(authorizationRequest.username(), authorizationRequest.password());
    }
}