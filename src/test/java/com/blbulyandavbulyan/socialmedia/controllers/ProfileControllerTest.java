package com.blbulyandavbulyan.socialmedia.controllers;

import com.blbulyandavbulyan.socialmedia.dtos.page.PageRequest;
import com.blbulyandavbulyan.socialmedia.dtos.subcriptions.SubscriptionResponse;
import com.blbulyandavbulyan.socialmedia.services.SubscriptionService;
import com.blbulyandavbulyan.socialmedia.testutils.AuthorizationUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class ProfileControllerTest {
    private final String path = "/api/v1/profile";
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private SubscriptionService subscriptionService;
    @Autowired
    private AuthorizationUtils authorizationUtils;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getUnwatchedSubscriptions() throws Exception {
        String targetUsername = "david";
        PageRequest pageRequest = new PageRequest(1, 4, Sort.Direction.ASC);
        Sort.Direction direction = Sort.Direction.ASC;
        int pageIndex = 0;
        int totalPages = 1;
        boolean first = true;
        boolean last = false;
        List<SubscriptionResponse> subscriptionResponses = List.of(
                new SubscriptionResponse("andrey", Instant.now(), false),
                new SubscriptionResponse("evgeniy", Instant.now(), false),
                new SubscriptionResponse("anatoly", Instant.now(), false)
        );
        var subscriptionResponsePage = Mockito.mock(org.springframework.data.domain.Page.class);
        when(subscriptionResponsePage.getTotalPages()).thenReturn(totalPages);
        when(subscriptionResponsePage.getSize()).thenReturn(pageRequest.pageSize());
        when(subscriptionResponsePage.getTotalElements()).thenReturn((long) subscriptionResponses.size());
        when(subscriptionResponsePage.getNumber()).thenReturn(pageIndex);
        when(subscriptionResponsePage.getContent()).thenReturn(subscriptionResponses);
        when(subscriptionResponsePage.isFirst()).thenReturn(first);
        when(subscriptionResponsePage.isLast()).thenReturn(last);
        when(subscriptionService.getUnwatchedSubscriptions(targetUsername,
                pageRequest.pageNumber(),
                pageRequest.pageSize(),
                Sort.Direction.ASC)).thenReturn(subscriptionResponsePage);
        var expectedPageResponse = new com.blbulyandavbulyan.socialmedia.dtos.Page<>(
                subscriptionResponses,
                totalPages,
                subscriptionResponses.size(),
                pageRequest.pageSize(),
                pageIndex + 1,
                last,
                first
        );
        mockMvc.perform(get(path + "/subscriptions/unwatched")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pageRequest))
                        .headers(authorizationUtils.generateHeaders(targetUsername, List.of()))
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedPageResponse), true))
                .andDo(document("normal-get-unwatched-subscriptions",
                        resourceDetails().description("Get unwatched subscriptions").tag("subscription"),
                        requestFields(
                                fieldWithPath("pageNumber").type(JsonFieldType.NUMBER).description("Number of page(starts from 1)"),
                                fieldWithPath("pageSize").type(JsonFieldType.NUMBER).description("Size of pages"),
                                fieldWithPath("direction").type(JsonFieldType.STRING).description("The direction of sorting, ASC - ascended, DESC - descended")
                        ),
                        responseFields(
                                fieldWithPath("content").type(JsonFieldType.ARRAY).description("This array contains requested content"),
                                fieldWithPath("content[].subscriberUsername").type(JsonFieldType.STRING).description("Name of user, who is subscribed to you"),
                                fieldWithPath("content[].creationDate").type(JsonFieldType.STRING).description("Date and time of subscribing"),
                                fieldWithPath("content[].viewed").type(JsonFieldType.BOOLEAN).description("Indicates, if this subscription watched or not"),
                                fieldWithPath("totalPages").type(JsonFieldType.NUMBER).description("Total count of pages with this page size"),
                                fieldWithPath("totalElements").type(JsonFieldType.NUMBER).description("Total count of unwatched subscriptions, between you and given username"),
                                fieldWithPath("pageSize").type(JsonFieldType.NUMBER).description("The given page size in the request"),
                                fieldWithPath("number").type(JsonFieldType.NUMBER).description("Number of requested page(which you give in the request)"),
                                fieldWithPath("first").type(JsonFieldType.BOOLEAN).description("Indicates is this first page or not"),
                                fieldWithPath("last").type(JsonFieldType.BOOLEAN).description("Indicates is this last page or not")
                        )
                ));
    }
}