package com.blbulyandavbulyan.socialmedia.controllers;

import com.blbulyandavbulyan.socialmedia.annotations.validation.ValidUserName;
import com.blbulyandavbulyan.socialmedia.services.SubscriptionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/subscriptions")
@AllArgsConstructor
public class SubscriptionController {
    private SubscriptionService subscriptionService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void subscribe(@ValidUserName @RequestParam("target") String target, Principal principal) {
        subscriptionService.create(principal.getName(), target);
    }

    @DeleteMapping
    public void unsubscribe(@ValidUserName @RequestParam("target") String target, Principal principal) {
        subscriptionService.unsubscribe(principal.getName(), target);
    }

    @PatchMapping
    public void markSubscriptionAsViewed(@ValidUserName @RequestParam("target") String target, Principal principal) {
        subscriptionService.markSubscriptionAsViewed(principal.getName(), target);
    }
}
