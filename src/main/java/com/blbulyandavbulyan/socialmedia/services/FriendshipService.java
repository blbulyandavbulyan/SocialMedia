package com.blbulyandavbulyan.socialmedia.services;

import com.blbulyandavbulyan.socialmedia.dtos.IFriend;
import com.blbulyandavbulyan.socialmedia.dtos.page.PageRequest;
import com.blbulyandavbulyan.socialmedia.repositories.SubscriptionRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FriendshipService {
    private SubscriptionService subscriptionService;
    private SubscriptionRepository subscriptionRepository;
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

    /**
     * Получает друзей заданного пользователя
     *
     * @param name        имя пользователя, чьих друзей мы ищем
     * @param pageRequest запрос страницы(подразумевается моё DTO)
     * @return страницу, содержащую друзей
     */
    public Page<IFriend> getFriends(String name, PageRequest pageRequest) {
        // TODO: 07.09.2023 добавить здесь вызов метода из репозитория subscriptionRepository или прокинуть этот вызов через сервис
        return subscriptionRepository.findAllFriends(name,
                org.springframework.data.domain.PageRequest.of(pageRequest.pageNumber() - 1,
                        pageRequest.pageSize(),
                        Sort.by(pageRequest.direction(), "friendshipStartDate"))
        );
    }
}
