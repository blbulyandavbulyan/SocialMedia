package com.blbulyandavbulyan.socialmedia.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FriendshipService implements IFriendService {
    private SubscriptionService subscriptionService;

    public boolean areTheyFriends(String first, String second) {
        return subscriptionService.isFirstSubscriberForSecond(first, second)
                && subscriptionService.isFirstSubscriberForSecond(second, first);
    }

}
