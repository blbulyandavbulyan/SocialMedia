package com.blbulyandavbulyan.socialmedia.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class FriendshipService {
    private SubscriptionService subscriptionService;

    public boolean areTheyFriends(String first, String second) {
        return subscriptionService.isFirstSubscriberForSecond(first, second)
                && subscriptionService.isFirstSubscriberForSecond(second, first);
    }

    @Transactional
    public void acceptFriendshipRequest(String friendshipAcceptor, String friendshipSender) {
        subscriptionService.markSubscriptionAsViewed(friendshipAcceptor, friendshipSender);
        // TODO: 01.09.2023 подумать над тем, должна ли быть эта подписка просмотрена или нет
        subscriptionService.create(friendshipSender, friendshipSender);
    }

}
