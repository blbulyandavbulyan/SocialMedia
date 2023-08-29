package com.blbulyandavbulyan.socialmedia.controllers;

import com.blbulyandavbulyan.socialmedia.dtos.publications.PublicationRequest;
import com.blbulyandavbulyan.socialmedia.entites.User;
import com.blbulyandavbulyan.socialmedia.repositories.UserRepository;
import com.blbulyandavbulyan.socialmedia.services.FileService;
import com.blbulyandavbulyan.socialmedia.services.PublicationService;
import com.blbulyandavbulyan.socialmedia.utils.JWTTokenUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PublicationControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @SpyBean
    private PublicationService publicationService;
    @Mock
    private FileService fileService;
    @Autowired
    private JWTTokenUtils jwtTokenUtils;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    private void init() {

    }

    @Test
    void normalCreatePublication() throws Exception {
        String fakeUserName = "david";
        userRepository.save(new User(fakeUserName, "3dfdsfsdfs13", "dadfafew@gmail.com"));
        PublicationRequest publicationRequest = new PublicationRequest("test publication", "very longlong text", List.of());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + jwtTokenUtils.generateToken(fakeUserName, List.of()));
        mockMvc.perform(post("/api/v1/publications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(publicationRequest))
                        .headers(headers)
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(
                        document("normal-creating-publication", resourceDetails().description("Create publication").tag("publication"),
                                requestFields(
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("Title of new publication"),
                                        fieldWithPath("text").type(JsonFieldType.STRING).description("Text of new publication"),
                                        fieldWithPath("filesUUIDs").type(JsonFieldType.ARRAY).description("Array of UUIDs of attached files")
                                ),
                                responseFields(
                                        fieldWithPath("publicationId").type(JsonFieldType.NUMBER).description("Id of new publication"),
                                        fieldWithPath("creationDate").type(JsonFieldType.STRING).description("Creation date and time")
                                )
                        )
                );

        Mockito.verify(publicationService, Mockito.only()).create(publicationRequest, fakeUserName);
    }
}
