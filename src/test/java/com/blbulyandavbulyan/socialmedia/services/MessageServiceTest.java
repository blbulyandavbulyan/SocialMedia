package com.blbulyandavbulyan.socialmedia.services;

import com.blbulyandavbulyan.socialmedia.dtos.messages.MessageResponse;
import com.blbulyandavbulyan.socialmedia.entites.Message;
import com.blbulyandavbulyan.socialmedia.entites.User;
import com.blbulyandavbulyan.socialmedia.exceptions.messages.SendingMessageToNonFriendException;
import com.blbulyandavbulyan.socialmedia.repositories.MessageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

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

    @Test
    void sendMessageShouldThrowExceptionIfUsersAreNotFriends() {
        String receiverName = "david";
        String senderName = "andrey";
        Mockito.when(iFriendService.areTheyFriends(senderName, receiverName)).thenReturn(false);
        assertThrows(SendingMessageToNonFriendException.class, () -> underTest.sendMessage(senderName, receiverName, "test"));
        Mockito.verify(iFriendService, Mockito.only()).areTheyFriends(senderName, receiverName);
        Mockito.verify(messageRepository, Mockito.never()).save(any());
    }

    @Test
    void normalSendMessage() {
        String senderName = "david";
        String receiverName = "andrey";
        Mockito.when(iFriendService.areTheyFriends(senderName, receiverName)).thenReturn(true);
        User expectedSender = new User();
        expectedSender.setUsername(senderName);
        User expectedReceiver = new User();
        String expectedText = "test tesxtwe da";
        expectedReceiver.setUsername(receiverName);
        Mockito.when(userService.findByUserName(senderName)).thenReturn(Optional.of(expectedSender));
        Mockito.when(userService.findByUserName(receiverName)).thenReturn(Optional.of(expectedReceiver));
        assertDoesNotThrow(() -> underTest.sendMessage(senderName, receiverName, expectedText));
        ArgumentCaptor<Message> messageArgumentCaptor = ArgumentCaptor.forClass(Message.class);
        Mockito.verify(messageRepository, Mockito.only()).save(messageArgumentCaptor.capture());
        Mockito.verify(iFriendService, Mockito.only()).areTheyFriends(senderName, receiverName);
        Message actualMessage = messageArgumentCaptor.getValue();
        assertEquals(expectedText, actualMessage.getText());
        assertEquals(expectedSender, actualMessage.getSender());
        assertEquals(expectedReceiver, actualMessage.getReceiver());
    }
}