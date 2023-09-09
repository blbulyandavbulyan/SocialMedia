package com.blbulyandavbulyan.socialmedia.controllers;

import com.blbulyandavbulyan.socialmedia.annotations.validation.user.ValidUserName;
import com.blbulyandavbulyan.socialmedia.dtos.Page;
import com.blbulyandavbulyan.socialmedia.dtos.messages.CreateMessageRequest;
import com.blbulyandavbulyan.socialmedia.dtos.messages.MessageCreatedResponse;
import com.blbulyandavbulyan.socialmedia.dtos.messages.MessageResponse;
import com.blbulyandavbulyan.socialmedia.entites.Message;
import com.blbulyandavbulyan.socialmedia.services.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/messages")
@AllArgsConstructor
public class MessagesController {
    private MessageService messageService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MessageCreatedResponse sendMessage(@Validated @RequestBody CreateMessageRequest createMessageRequest, Principal principal) {
        Message message = messageService.sendMessage(principal.getName(), createMessageRequest.receiverName(), createMessageRequest.text());
        return new MessageCreatedResponse(message.getId(), createMessageRequest.receiverName(), message.getSendingDate());
    }

    @PatchMapping("/{messageId}")
    public void markMessageAsRead(@PathVariable Long messageId, Principal principal) {
        messageService.markMessageAsRead(principal.getName(), messageId);
    }

    @GetMapping
    public Page<MessageResponse> getMessages(@ValidUserName @RequestParam("target") String target,
                                             @RequestParam Integer pageNumber,
                                             @RequestParam(defaultValue = "10") Integer pageSize,
                                             Principal principal) {
        return Page.of(messageService.getMessageForReceiver(principal.getName(), target, PageRequest.of(pageNumber - 1, pageSize)));
    }
}
