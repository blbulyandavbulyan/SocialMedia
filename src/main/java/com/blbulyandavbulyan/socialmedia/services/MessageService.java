package com.blbulyandavbulyan.socialmedia.services;

import com.blbulyandavbulyan.socialmedia.dtos.messages.MessageResponse;
import com.blbulyandavbulyan.socialmedia.entites.Message;
import com.blbulyandavbulyan.socialmedia.exceptions.messages.SendingMessageToNonFriendException;
import com.blbulyandavbulyan.socialmedia.exceptions.messages.YouAreNotReceiverOfThisMessage;
import com.blbulyandavbulyan.socialmedia.repositories.MessageRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MessageService {
    private MessageRepository messageRepository;
    private UserService userService;
    private IFriendService iFriendService;

    public Page<MessageResponse> getMessageForReceiver(String receiverName, String senderName, Pageable pageable) {
        return messageRepository.findByReceiverUsernameAndSenderUsernameOrderBySendingDateDesc(receiverName, senderName, pageable);
    }

    public void sendMessage(String senderName, String receiverName, String text) {
        if (iFriendService.areTheyFriends(senderName, receiverName)) {
            // TODO: 02.09.2023 Выбросить исключение о том что отправитель не найден
            // TODO: 02.09.2023 Выбросить исключение о том что получатель не найден
            Message message = new Message(userService.findByUserName(senderName).orElseThrow(), userService.findByUserName(receiverName).orElseThrow(), text);
            messageRepository.save(message);
        } else
            throw new SendingMessageToNonFriendException("You can send messages only to friends!");
    }

    public void markMessageAsRead(String receiverUserName, Long messageId) {
        String realReceiverUsername = messageRepository.findReceiverUsernameById(messageId).orElseThrow();// TODO: 02.09.2023 бросить исключение если такого сообщения нет
        if (realReceiverUsername.equals(receiverUserName)) {
            messageRepository.updateReadById(messageId, true);
        } else
            throw new YouAreNotReceiverOfThisMessage("You can't change read status, because you are not receiver of this message");// TODO: 02.09.2023 бросить исключение если пытается пометить прочитанным не получатель
    }
}
