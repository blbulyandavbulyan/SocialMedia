package com.blbulyandavbulyan.socialmedia.controllers;

import com.blbulyandavbulyan.socialmedia.dtos.IFriend;
import com.blbulyandavbulyan.socialmedia.dtos.page.PageRequest;
import com.blbulyandavbulyan.socialmedia.dtos.page.PageResponse;
import com.blbulyandavbulyan.socialmedia.dtos.subcriptions.SubscriptionResponse;
import com.blbulyandavbulyan.socialmedia.services.FriendshipService;
import com.blbulyandavbulyan.socialmedia.services.SubscriptionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/profile")
@AllArgsConstructor
public class ProfileController {
    private SubscriptionService subscriptionService;
    private FriendshipService friendshipService;
    @GetMapping("/subscriptions/unwatched")
    public PageResponse<SubscriptionResponse> getUnwatchedSubscriptions(@RequestBody PageRequest pageRequest, Principal principal) {
        return PageResponse.of(subscriptionService.getUnwatchedSubscriptions(principal.getName(), pageRequest));
    }

    @GetMapping("/friends")
    public PageResponse<IFriend> getFriends(@RequestBody PageRequest pageRequest, Principal principal) {
        return PageResponse.of(friendshipService.getFriends(principal.getName(), pageRequest));
    }

    @GetMapping("/subscriptions/all")
    public PageResponse<SubscriptionResponse> getAllSubscriptions(@RequestBody PageRequest pageRequest, Principal principal) {
        throw new UnsupportedOperationException();
    }
}
