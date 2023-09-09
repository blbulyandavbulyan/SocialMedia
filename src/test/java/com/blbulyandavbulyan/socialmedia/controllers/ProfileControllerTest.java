package com.blbulyandavbulyan.socialmedia.controllers;

import com.blbulyandavbulyan.socialmedia.dtos.Friend;
import com.blbulyandavbulyan.socialmedia.dtos.IFriend;
import com.blbulyandavbulyan.socialmedia.dtos.page.PageRequest;
import com.blbulyandavbulyan.socialmedia.dtos.page.PageResponse;
import com.blbulyandavbulyan.socialmedia.dtos.subcriptions.SubscriptionResponse;
import com.blbulyandavbulyan.socialmedia.services.FriendshipService;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static com.blbulyandavbulyan.socialmedia.testutils.PageUtils.generatePageResponseDescription;
import static com.blbulyandavbulyan.socialmedia.testutils.PageUtils.getMockPage;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class ProfileControllerTest {
    private final String path = "/api/v1/profile";
    private final RequestFieldsSnippet pageRequestFieldSnippet = requestFields(
            fieldWithPath("pageNumber").type(JsonFieldType.NUMBER).description("Number of page(starts from 1)"),
            fieldWithPath("pageSize").type(JsonFieldType.NUMBER).description("Size of pages"),
            fieldWithPath("direction").type(JsonFieldType.STRING).description("The direction of sorting, ASC - ascended, DESC - descended")
    );
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private SubscriptionService subscriptionService;
    @MockBean
    private FriendshipService friendshipService;
    @Autowired
    private AuthorizationUtils authorizationUtils;
    @Autowired
    private ObjectMapper objectMapper;



    @Test
    void getUnwatchedSubscriptions() throws Exception {
        String targetUsername = "david";
        PageRequest pageRequest = new PageRequest(1, 4, Sort.Direction.ASC);
        int pageIndex = 0;
        int totalPages = 1;
        boolean first = true;
        boolean last = false;
        List<SubscriptionResponse> subscriptionResponses = List.of(
                new SubscriptionResponse("andrey", Instant.now(), false),
                new SubscriptionResponse("evgeniy", Instant.now(), false),
                new SubscriptionResponse("anatoly", Instant.now(), false)
        );
        var expectedPageResponse = new PageResponse<>(
                subscriptionResponses,
                totalPages,
                subscriptionResponses.size(),
                pageRequest.pageSize(),
                pageIndex + 1,
                last,
                first
        );
        var subscriptionResponsePage = getMockPage(expectedPageResponse);
        when(subscriptionService.getUnwatchedSubscriptions(targetUsername, pageRequest)).thenReturn(subscriptionResponsePage);
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
                        pageRequestFieldSnippet,
                        generatePageResponseDescription(
                                fieldWithPath("content[].subscriberUsername").type(JsonFieldType.STRING).description("Name of user, who is subscribed to you"),
                                fieldWithPath("content[].creationDate").type(JsonFieldType.STRING).description("Date and time of subscribing"),
                                fieldWithPath("content[].viewed").type(JsonFieldType.BOOLEAN).description("Indicates, if this subscription watched or not")
                        )
                ));
    }

    @Test
    void getFriends() throws Exception {
        String target = "david";
        PageRequest pageRequest = new PageRequest(1, 4, Sort.Direction.ASC);
        var expectedResponse = new PageResponse<IFriend>(
                List.of(
                        new Friend("test", Instant.now()),
                        new Friend("test2", Instant.now()),
                        new Friend("test3", Instant.now())
                ),
                1, 4, 10, 1, false, true
        );
        Page<IFriend> mockPage = getMockPage(expectedResponse);
        Mockito.when(friendshipService.getFriends(target, pageRequest))
                .thenReturn(mockPage);
        mockMvc.perform(get(path + "/friends")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pageRequest))
                        .headers(authorizationUtils.generateHeaders(target, List.of()))
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse), true))
                .andDo(document("normal-get-friends",
                        resourceDetails().description("Getting your friends").tag("friends"),
                        pageRequestFieldSnippet,
                        generatePageResponseDescription(
                                fieldWithPath("content[].friendUsername").type(JsonFieldType.STRING).description("Username of your friend"),
                                fieldWithPath("content[].friendshipStartDate").type(JsonFieldType.STRING).description("Date and time, when your friendship was started")
                        )
                ));
    }
}