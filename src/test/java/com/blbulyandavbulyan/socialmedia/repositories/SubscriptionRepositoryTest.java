package com.blbulyandavbulyan.socialmedia.repositories;

import com.blbulyandavbulyan.socialmedia.dtos.subcriptions.SubscriptionResponse;
import com.blbulyandavbulyan.socialmedia.entites.Subscription;
import com.blbulyandavbulyan.socialmedia.entites.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class SubscriptionRepositoryTest {
    @Autowired
    private SubscriptionRepository subscriptionRepository;
    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        subscriptionRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void findByTargetUsernameAndViewedIsFalse() {
        List<User> users = new ArrayList<>();
        List<Subscription> subscriptions = new ArrayList<>();

        // Создаем 5 пользователей
        String targetUsername = "user1";
        users.add(new User(targetUsername, "password1234", "user1@example.com"));
        users.add(new User("user2", "password1234", "user2@example.com"));
        users.add(new User("user3", "password1234", "user3@example.com"));
        users.add(new User("user4", "password1234", "user4@example.com"));
        users.add(new User("user5", "password1234", "user5@example.com"));
        userRepository.saveAllAndFlush(users);
        // Создаем подписки между пользователями
        subscriptions.add(new Subscription("user2", targetUsername));
        subscriptions.add(new Subscription("user3", targetUsername));
        subscriptions.add(new Subscription("user4", targetUsername));
        subscriptions.add(new Subscription("user5", targetUsername));
        subscriptions = subscriptionRepository.saveAllAndFlush(subscriptions);
        var subscriptionResponses = subscriptions.stream().map(s -> new SubscriptionResponse(s.getSubscriberUsername(), s.getCreationDate(), s.isViewed())).toList();
        Page<SubscriptionResponse> subscriptionResponsePage = subscriptionRepository.findByTargetUsernameAndViewedIsFalse(targetUsername, PageRequest.of(0, 4));
        assertEquals(subscriptions.size(), subscriptionResponsePage.getTotalElements());
        assertEquals(1, subscriptionResponsePage.getTotalPages());
        assertTrue(subscriptionResponsePage.hasContent());
        assertTrue(subscriptionResponsePage.getContent().containsAll(subscriptionResponses));
    }
}