package com.blbulyandavbulyan.socialmedia.services;

import com.blbulyandavbulyan.socialmedia.dtos.messages.MessageResponse;
import com.blbulyandavbulyan.socialmedia.entites.Message;
import com.blbulyandavbulyan.socialmedia.entites.User;
import com.blbulyandavbulyan.socialmedia.exceptions.messages.MessageNotFoundException;
import com.blbulyandavbulyan.socialmedia.exceptions.messages.SendingMessageToNonFriendException;
import com.blbulyandavbulyan.socialmedia.exceptions.messages.YouAreNotReceiverOfThisMessage;
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

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;

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
        Mockito.when(messageRepository.getMessagesBetweenUsers(receiverName, senderName, pageable)).thenReturn(expectedPage);
        Page<MessageResponse> actualPage = underTest.getMessageForReceiver(receiverName, senderName, pageable);
        Mockito.verify(messageRepository, Mockito.only()).getMessagesBetweenUsers(receiverName, senderName, pageable);
        assertSame(expectedPage, actualPage);
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
        Long expectedId = 3232L;
        Instant expectedSendingDate = Instant.now();
        Mockito.when(iFriendService.areTheyFriends(senderName, receiverName)).thenReturn(true);
        Mockito.when(messageRepository.save(isA(Message.class))).then((invocation -> {
            Message m = invocation.getArgument(0);
            m.setSendingDate(expectedSendingDate);
            m.setId(expectedId);
            return m;
        }));
        User expectedSender = new User();
        expectedSender.setUsername(senderName);
        User expectedReceiver = new User();
        String expectedText = "test tesxtwe da";
        expectedReceiver.setUsername(receiverName);
        Mockito.when(userService.findByUserName(senderName)).thenReturn(Optional.of(expectedSender));
        Mockito.when(userService.findByUserName(receiverName)).thenReturn(Optional.of(expectedReceiver));
        Message sendMessageResult = underTest.sendMessage(senderName, receiverName, expectedText);
        ArgumentCaptor<Message> messageArgumentCaptor = ArgumentCaptor.forClass(Message.class);
        Mockito.verify(messageRepository, Mockito.only()).save(messageArgumentCaptor.capture());
        Mockito.verify(iFriendService, Mockito.only()).areTheyFriends(senderName, receiverName);
        //проверка сохранённого сообщения
        Message savingMessage = messageArgumentCaptor.getValue();
        assertEquals(expectedText, savingMessage.getText());
        assertEquals(expectedSender, savingMessage.getSender());
        assertEquals(expectedReceiver, savingMessage.getReceiver());
        //проверка сообщения, которое вернул нам метод отправки в качестве результата
        assertEquals(expectedText, sendMessageResult.getText());
        assertEquals(expectedSender, sendMessageResult.getSender());
        assertEquals(expectedReceiver, sendMessageResult.getReceiver());
        assertEquals(expectedId, sendMessageResult.getId());
        assertEquals(expectedSendingDate, sendMessageResult.getSendingDate());
    }

    @Test
    void markMessageAsReadShouldThrowExceptionIfUserIsNotItsReceiver() {
        String receiverUsername = "david";
        Long messageId = 1L;
        Mockito.when(messageRepository.findReceiverUsernameById(messageId)).thenReturn(Optional.of("anatoly"));
        assertThrows(YouAreNotReceiverOfThisMessage.class, () -> underTest.markMessageAsRead(receiverUsername, messageId));
        Mockito.verify(messageRepository, Mockito.only()).findReceiverUsernameById(messageId);
    }

    @Test
    void normalMarkMessageAsRead() {
        String receiverUsername = "david";
        Long messageId = 1L;
        Mockito.when(messageRepository.findReceiverUsernameById(messageId)).thenReturn(Optional.of(receiverUsername));
        assertDoesNotThrow(() -> underTest.markMessageAsRead(receiverUsername, messageId));
        Mockito.verify(messageRepository, Mockito.times(1)).findReceiverUsernameById(messageId);
        Mockito.verify(messageRepository, Mockito.times(1)).updateReadById(messageId, true);
    }

    @Test
    void markMessageAsReadIfItDoesNotExists() {
        String receiverUsername = "david";
        Long messageId = 1L;
        Mockito.when(messageRepository.findReceiverUsernameById(messageId)).thenReturn(Optional.empty());
        assertThrows(MessageNotFoundException.class, () -> underTest.markMessageAsRead(receiverUsername, messageId));
        Mockito.verify(messageRepository, Mockito.only()).findReceiverUsernameById(messageId);
    }
}