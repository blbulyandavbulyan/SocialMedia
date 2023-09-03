package com.blbulyandavbulyan.socialmedia.services;

import com.blbulyandavbulyan.socialmedia.dtos.messages.MessageResponse;
import com.blbulyandavbulyan.socialmedia.repositories.MessageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.assertSame;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {
    @Mock
    private IFriendService iFriendService;
    @Mock
    private MessageRepository messageRepository;
    @Mock
    private UserService userService;
    @InjectMocks
    private MessageService underTest;

    @Test
    void getMessageForReceiver() {
        String receiverName = "david";
        String senderName = "andrey";
        Pageable pageable = PageRequest.of(1, 10);
        Page<MessageResponse> expectedPage = Mockito.mock(Page.class);
        Mockito.when(messageRepository.findByReceiverUsernameAndSenderUsernameOrderBySendingDateDesc(receiverName, senderName, pageable)).thenReturn(expectedPage);
        Page<MessageResponse> actualPage = underTest.getMessageForReceiver(receiverName, senderName, pageable);
        assertSame(expectedPage, actualPage);
        Mockito.verify(messageRepository, Mockito.only()).findByReceiverUsernameAndSenderUsernameOrderBySendingDateDesc(receiverName, senderName, pageable);
    }
}