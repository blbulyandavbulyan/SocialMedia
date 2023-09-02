package com.blbulyandavbulyan.socialmedia.controllers;

import com.blbulyandavbulyan.socialmedia.entites.Subscription;
import com.blbulyandavbulyan.socialmedia.entites.User;
import com.blbulyandavbulyan.socialmedia.entites.keys.SubscriptionPK;
import com.blbulyandavbulyan.socialmedia.repositories.SubscriptionRepository;
import com.blbulyandavbulyan.socialmedia.repositories.UserRepository;
import com.blbulyandavbulyan.socialmedia.services.SubscriptionService;
import com.blbulyandavbulyan.socialmedia.testutils.AuthorizationUtils;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.request.RequestDocumentation.formParameters;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class SubscriptionControllerTest {
    private final String apiPath = "/api/v1/subscriptions";
    @SpyBean
    private SubscriptionService subscriptionService;
    @Autowired
    private SubscriptionRepository subscriptionRepository;
    @Autowired
    private AuthorizationUtils authorizationUtils;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MockMvc mockMvc;
    private User user1;
    private User user2;

    private final ParameterDescriptor targetFormParameterSnippet = parameterWithName("target").description("Target username");
    private HttpHeaders httpHeaders;

    @PostConstruct
    public void init() {
        user1 = userRepository.save(new User("david", "1241421333131", "test@gmail.com"));
        user2 = userRepository.save(new User("andrey", "23143131431", "other@gmail.com"));
    }

    @AfterEach
    public void clear() {
        subscriptionRepository.deleteAll();
    }

    @BeforeEach
    void setUp() {
        httpHeaders = authorizationUtils.generateHeaders(user1.getUsername(), user1.getAuthorities());
    }

    @Test
    void normalCreateSubscription() throws Exception {
        mockMvc.perform(post(apiPath).headers(httpHeaders).param("target", user2.getUsername()))
                .andExpect(status().isCreated())
                .andDo(
                        document("normal-create-subscription",
                                resourceDetails().description("Subscribe to target user").tag("subscription"),
                                formParameters(targetFormParameterSnippet)
                        )
                );
        Mockito.verify(subscriptionService, Mockito.only()).create(user1.getUsername(), user2.getUsername());
        assertTrue(subscriptionRepository.existsById(new SubscriptionPK(user1.getUsername(), user2.getUsername())));
    }

    @Test
    void normalDeleteSubscription() throws Exception {
        subscriptionRepository.save(new Subscription(user1.getUsername(), user2.getUsername()));
        mockMvc.perform(delete(apiPath).headers(httpHeaders).param("target", user2.getUsername()))
                .andExpect(status().isOk())
                .andDo(
                        document("normal-delete-subscription",
                                resourceDetails().description("Unsubscribe from target").tag("subscription"),
                                formParameters(targetFormParameterSnippet)
                        )
                );
        Mockito.verify(subscriptionService, Mockito.only()).unsubscribe(user1.getUsername(), user2.getUsername());
        assertFalse(subscriptionRepository.existsById(new SubscriptionPK(user1.getUsername(), user2.getUsername())));
    }
}