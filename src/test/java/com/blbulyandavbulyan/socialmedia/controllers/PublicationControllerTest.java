package com.blbulyandavbulyan.socialmedia.controllers;

import com.blbulyandavbulyan.socialmedia.dtos.publications.PublicationRequest;
import com.blbulyandavbulyan.socialmedia.entites.Publication;
import com.blbulyandavbulyan.socialmedia.entites.User;
import com.blbulyandavbulyan.socialmedia.repositories.PublicationRepository;
import com.blbulyandavbulyan.socialmedia.repositories.UserRepository;
import com.blbulyandavbulyan.socialmedia.services.FileService;
import com.blbulyandavbulyan.socialmedia.services.PublicationService;
import com.blbulyandavbulyan.socialmedia.utils.JWTTokenUtils;
import com.epages.restdocs.apispec.ResourceSnippetDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
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
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.request.PathParametersSnippet;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
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
    @Autowired
    private PublicationRepository publicationRepository;
    private final String fakeUserName = "david";
    private User fakeUser;
    private final ResourceSnippetDetails deleteResourceDetails = resourceDetails().description("Delete publication").tag("publication").summary("You can delete publication, if it exists and you authorized and own this publication");
    private final PathParametersSnippet deletePathParameters = pathParameters(parameterWithName("publicationId").description("Id of publication to delete"));
    private final ResourceSnippetDetails createResourceDetails = resourceDetails().description("Create publication").tag("publication");
    private final RequestFieldsSnippet createPublicationRequestFields = requestFields(
            fieldWithPath("title").type(JsonFieldType.STRING).description("Title of new publication"),
            fieldWithPath("text").type(JsonFieldType.STRING).description("Text of new publication"),
            fieldWithPath("filesUUIDs").type(JsonFieldType.ARRAY).description("Array of UUIDs of attached files")
    );

    @PostConstruct
    public void init() {
        fakeUser = userRepository.save(new User(fakeUserName, "3dfdsfsdfs13", "dadfafew@gmail.com"));
    }

    @Test
    void normalCreatePublication() throws Exception {
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
                        document("normal-creating-publication", createResourceDetails,
                                createPublicationRequestFields,
                                responseFields(
                                        fieldWithPath("publicationId").type(JsonFieldType.NUMBER).description("Id of new publication"),
                                        fieldWithPath("publicationDate").type(JsonFieldType.STRING).description("Creation date and time")
                                )
                        )
                );

        Mockito.verify(publicationService, Mockito.only()).create(publicationRequest, fakeUserName);
    }
    @Test
    void createPublicationIfYouNotAuthorized() throws Exception {
        PublicationRequest publicationRequest = new PublicationRequest("test publication", "very longlong text", List.of());
        mockMvc.perform(post("/api/v1/publications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(publicationRequest))
                )
                .andExpect(status().isUnauthorized())
                .andDo(
                        document(
                                "create-publication-if-you-not-authorized",
                                createResourceDetails,
                                createPublicationRequestFields
                        )
                );
    }

    @Test
    void normalDeletePublication() throws Exception {
        Long publicationForDelete = publicationRepository.save(new Publication("Blablabla", "Test text", fakeUser, List.of())).getId();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + jwtTokenUtils.generateToken(fakeUserName, List.of()));
        mockMvc.perform(delete("/api/v1/publications/{publicationId}", publicationForDelete).headers(headers))
                .andExpect(status().isOk())
                .andDo(
                        document(
                                "normal-delete-publication",
                                deleteResourceDetails,
                                deletePathParameters
                        )
                );
        Mockito.verify(publicationService, Mockito.only()).delete(publicationForDelete, fakeUserName);
        assertFalse(publicationRepository.existsById(publicationForDelete));
    }

    @Test
    void deletePublicationIfYouDoNotAuthorized() throws Exception {
        Long publicationForDelete = publicationRepository.save(new Publication("Blablabla", "Test text", fakeUser, List.of())).getId();
        mockMvc.perform(delete("/api/v1/publications/{publicationId}", publicationForDelete))
                .andExpect(status().isUnauthorized())
                .andDo(
                        document(
                                "delete-publication-if-you-dont-own-it",
                                deleteResourceDetails,
                                deletePathParameters
                        )
                );
    }
}
