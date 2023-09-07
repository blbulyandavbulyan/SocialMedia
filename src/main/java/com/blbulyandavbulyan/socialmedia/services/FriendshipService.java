package com.blbulyandavbulyan.socialmedia.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FriendshipService {
    private SubscriptionService subscriptionService;

    /**
     * Проверяет друзья ли пользователи
     *
     * @param username1 имя первого пользователя
     * @param username2 имя второго пользователя
     * @return true если пользователи друзья
     */
    public boolean areTheyFriends(String username1, String username2) {
        return subscriptionService.isFirstSubscriberForSecond(username1, username2)
                && subscriptionService.isFirstSubscriberForSecond(username2, username1);
    }

}
