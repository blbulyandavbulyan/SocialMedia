package com.blbulyandavbulyan.socialmedia.controllers;

import com.blbulyandavbulyan.socialmedia.dtos.Page;
import com.blbulyandavbulyan.socialmedia.dtos.page.PageRequest;
import com.blbulyandavbulyan.socialmedia.dtos.subcriptions.SubscriptionResponse;
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

    @GetMapping("/subscriptions/unwatched")
    public Page<SubscriptionResponse> getUnwatchedSubscriptions(@RequestBody PageRequest pageRequest, Principal principal) {
        return Page.of(subscriptionService.getUnwatchedSubscriptions(principal.getName(), pageRequest.pageNumber(), pageRequest.pageSize(), pageRequest.direction()));
    }
}
