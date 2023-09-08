package com.blbulyandavbulyan.socialmedia.services;

import com.blbulyandavbulyan.socialmedia.dtos.IFriend;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
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

    public Page<IFriend> getFriends(String name, int pageNumber, int pageSize, Sort.Direction direction) {
        // TODO: 07.09.2023 добавить здесь вызов метода из репозитория subscriptionRepository или прокинуть этот вызов через сервис
        throw new UnsupportedOperationException();
    }
}
