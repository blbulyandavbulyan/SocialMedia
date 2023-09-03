package com.blbulyandavbulyan.socialmedia.controllers;

import com.blbulyandavbulyan.socialmedia.dtos.Page;
import com.blbulyandavbulyan.socialmedia.dtos.messages.MessageResponse;
import com.blbulyandavbulyan.socialmedia.entites.Message;
import com.blbulyandavbulyan.socialmedia.entites.User;
import com.blbulyandavbulyan.socialmedia.repositories.MessageRepository;
import com.blbulyandavbulyan.socialmedia.repositories.UserRepository;
import com.blbulyandavbulyan.socialmedia.services.IFriendService;
import com.blbulyandavbulyan.socialmedia.services.MessageService;
import com.blbulyandavbulyan.socialmedia.utils.JWTTokenUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class MessagesControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MessageRepository messageRepository;
    @SpyBean
    private MessageService messageService;
    @Autowired
    private JWTTokenUtils jwtTokenUtils;
    private HttpHeaders httpHeaders;
    @Autowired
    private UserRepository userRepository;
    @MockBean
    private IFriendService iFriendService;
    private User user1;
    private User user2;
    @Autowired
    private ObjectMapper objectMapper;

    @PostConstruct
    void init() {
        user1 = userRepository.save(new User("david", "321413413312", "test@gmail.com"));
        user2 = userRepository.save(new User("andrey", "fdafewafwefwfw", "test32342@gmail.com"));
    }

    @BeforeEach
    public void initHttpHeaders() {
        httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Bearer " + jwtTokenUtils.generateToken(user1.getUsername(), user1.getAuthorities()));
    }

    @AfterEach
    void clear() {
        messageRepository.deleteAll();
    }

    @Test
    void getMessages() throws Exception {
        final int pageSize = 10;
        final int pageNumber = 1;
        List<Message> allSavedMessages = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            allSavedMessages.add(messageRepository.saveAndFlush(new Message(user2, user1, "Случайное сообщение #" + (i + 1))));
            Thread.sleep(500);
        }
        List<? extends MessageResponse> expectedMessages = allSavedMessages.stream()
                .sorted(Comparator.comparing(Message::getSendingDate).reversed())
                .limit(10).map(MessageResponseTestImpl::new).toList();
        var expectedContent = new Page<>(expectedMessages, 2, allSavedMessages.size(), pageSize, pageNumber, false, true);
        mockMvc.perform(
                        get("/api/v1/messages")
                                .queryParam("target", user2.getUsername())
                                .queryParam("pageNumber", Integer.toString(pageNumber))
                                .queryParam("pageSize", Integer.toString(pageSize))
                                .headers(httpHeaders)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedContent)))
                .andDo(
                        document("normal-get-messages",
                                resourceDetails().description("Get messages between you and given user").tag("message"),
                                queryParameters(
                                        parameterWithName("pageNumber").description("Number of requested page"),
                                        parameterWithName("pageSize").optional().description("Maximum number of elements per page"),
                                        parameterWithName("target").description("This is a name of user, which will be used for search messages between you and him")
                                ),
                                responseFields(
                                        fieldWithPath("content").type(JsonFieldType.ARRAY).description("This array contains requested messages"),
                                        fieldWithPath("content[].id").type(JsonFieldType.NUMBER).description("Message id"),
                                        fieldWithPath("content[].text").type(JsonFieldType.STRING).description("Message text"),
                                        fieldWithPath("content[].read").type(JsonFieldType.BOOLEAN).description("This field indicates, was this message read or not"),
                                        fieldWithPath("content[].sendingDate").type(JsonFieldType.STRING).description("Message sending date and time"),
                                        fieldWithPath("totalPages").type(JsonFieldType.NUMBER).description("Total count of pages with this page size"),
                                        fieldWithPath("totalElements").type(JsonFieldType.NUMBER).description("Total count of messages, between you and given username"),
                                        fieldWithPath("pageSize").type(JsonFieldType.NUMBER).description("The given page size in the request"),
                                        fieldWithPath("number").type(JsonFieldType.NUMBER).description("Number of requested page(which you give in the request)"),
                                        fieldWithPath("first").type(JsonFieldType.BOOLEAN).description("Indicates is this first page or not"),
                                        fieldWithPath("last").type(JsonFieldType.BOOLEAN).description("Indicates is this last page or not")
                                )
                        )
                );

    }

}