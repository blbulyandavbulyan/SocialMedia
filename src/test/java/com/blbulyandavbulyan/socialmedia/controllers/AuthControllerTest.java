package com.blbulyandavbulyan.socialmedia.controllers;

import com.blbulyandavbulyan.socialmedia.dtos.AuthorizationRequest;
import com.blbulyandavbulyan.socialmedia.dtos.RegistrationRequest;
import com.blbulyandavbulyan.socialmedia.entites.User;
import com.blbulyandavbulyan.socialmedia.repositories.UserRepository;
import com.blbulyandavbulyan.socialmedia.services.AuthService;
import com.epages.restdocs.apispec.ResourceSnippetDetails;
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
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
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
    private final ResourceSnippetDetails loginRequestDetailsSnippet = resourceDetails().description("Login request").tag("Registration and getting jwt token");
    private final ResourceSnippetDetails registrationDetailsSnippet = resourceDetails().description("Registration request").tag("Registration and getting jwt token");
    private final ResponseFieldsSnippet applicationErrorSnippet = responseFields(
            fieldWithPath("httpStatusCode").type(JsonFieldType.STRING).description("The http status code(in words, not number)"),
            fieldWithPath("message").type(JsonFieldType.STRING).description("The error message"),
            fieldWithPath("timestamp").type(JsonFieldType.STRING).description("The date and time of error happening")
    );
    private final RequestFieldsSnippet loginRequestSnippet = requestFields(
            fieldWithPath("username").type(JsonFieldType.STRING).description("User name for login"),
            fieldWithPath("password").type(JsonFieldType.STRING).description("Password password for login")
    );
    private final RequestFieldsSnippet registrationRequestSnippet = requestFields(
            fieldWithPath("username").type(JsonFieldType.STRING).description("The name of the new user"),
            fieldWithPath("password").type(JsonFieldType.STRING).description("The password of the new user"),
            fieldWithPath("email").type(JsonFieldType.STRING).description("The email of the new user")
    );

    @Test
    @DisplayName("normal login")
    public void normalLogin() throws Exception {
        AuthorizationRequest authorizationRequest = new AuthorizationRequest("david", "12345678632");
        User user = new User(authorizationRequest.username(), passwordEncoder.encode(authorizationRequest.password()), "test@gmail.com");
        Mockito.when(userRepository.findById(authorizationRequest.username())).thenReturn(Optional.of(user));
        mockMvc.perform(
                        post("/api/v1/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(authorizationRequest))
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(
                        document("normal-login",
                                loginRequestDetailsSnippet,
                                responseFields(fieldWithPath("token").description("Your JWT token")),
                                loginRequestSnippet
                        )
                );
        Mockito.verify(authService, Mockito.only()).authorize(authorizationRequest.username(), authorizationRequest.password());
    }

    @Test
    @DisplayName("login with not existing username")
    public void loginWhenUserWithThisNameNotFound() throws Exception {
        AuthorizationRequest authorizationRequest = new AuthorizationRequest("david", "12345678632");
        Mockito.when(userRepository.findById(authorizationRequest.username())).thenReturn(Optional.empty());
        mockMvc.perform(
                        post("/api/v1/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(authorizationRequest))
                )
                .andExpect(status().isUnauthorized())
                .andDo(document("login-with-incorrect-username", loginRequestDetailsSnippet, loginRequestSnippet, applicationErrorSnippet));
        Mockito.verify(authService, Mockito.only()).authorize(authorizationRequest.username(), authorizationRequest.password());
    }

    @Test
    @DisplayName("login with incorrect password")
    public void loginWithIncorrectPassword() throws Exception {
        AuthorizationRequest authorizationRequest = new AuthorizationRequest("david", "12345678632");
        User user = new User(authorizationRequest.username(), passwordEncoder.encode("dafaffwefa"), "test@gmail.com");
        Mockito.when(userRepository.findById(authorizationRequest.username())).thenReturn(Optional.of(user));
        mockMvc.perform(
                        post("/api/v1/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(authorizationRequest))
                )
                .andExpect(status().isUnauthorized())
                .andDo(document("login-with-incorrect-password", loginRequestDetailsSnippet, loginRequestSnippet, applicationErrorSnippet));
        Mockito.verify(authService, Mockito.only()).authorize(authorizationRequest.username(), authorizationRequest.password());
    }

    @Test
    @DisplayName("registration with valid data and not existing user")
    public void registrationWithValidDataAndNotExistingUser() throws Exception {
        RegistrationRequest registrationRequest = new RegistrationRequest("david", "32345325245", "test@gmail.com");
        Mockito.when(userRepository.existsByUsername(registrationRequest.username())).thenReturn(false);
        Mockito.when(userRepository.existsByEmail(registrationRequest.email())).thenReturn(false);
        mockMvc.perform(post("/api/v1/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationRequest))
                )
                .andExpect(status().isOk())
                .andDo(document("normal-registration-user", registrationDetailsSnippet, registrationRequestSnippet));
        Mockito.verify(authService, Mockito.only()).registerUser(registrationRequest.username(), registrationRequest.password(), registrationRequest.email());
    }

    @Test
    @DisplayName("registration when user with this name already exists")
    public void registrationWhenUserWithThisNameExists() throws Exception {
        RegistrationRequest registrationRequest = new RegistrationRequest("david", "32345325245", "test@gmail.com");
        Mockito.when(userRepository.existsByUsername(registrationRequest.username())).thenReturn(true);
        Mockito.when(userRepository.existsByEmail(registrationRequest.email())).thenReturn(false);
        mockMvc.perform(post("/api/v1/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationRequest))
                )
                .andExpect(status().isBadRequest())
                .andDo(document("registration-when-user-with-this-name-exists", registrationDetailsSnippet, registrationRequestSnippet, applicationErrorSnippet));
    }
}