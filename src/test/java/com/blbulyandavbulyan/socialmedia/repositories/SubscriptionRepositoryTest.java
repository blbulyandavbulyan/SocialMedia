package com.blbulyandavbulyan.socialmedia.repositories;

import com.blbulyandavbulyan.socialmedia.dtos.Friend;
import com.blbulyandavbulyan.socialmedia.dtos.IFriend;
import com.blbulyandavbulyan.socialmedia.dtos.subcriptions.SubscriptionResponse;
import com.blbulyandavbulyan.socialmedia.entites.Subscription;
import com.blbulyandavbulyan.socialmedia.entites.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.*;

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

    @Test
    void findAllFriends() {
        User user1 = userRepository.saveAndFlush(new User("user1", "password1234", "user1@example.com"));
        User user2 = userRepository.saveAndFlush(new User("user2", "password1234", "user2@example.com"));
        User user3 = userRepository.saveAndFlush(new User("user3", "password1234", "user3@example.com"));
        User user4 = userRepository.saveAndFlush(new User("user4", "password1234", "user4@example.com"));
        User user5 = userRepository.saveAndFlush(new User("user5", "password1234", "user5@example.com"));
        //пусть user1 и user2 взаимно подписаны друг на друга(друзья)
        Subscription user1ToUser2 = subscriptionRepository.saveAndFlush(new Subscription(user1.getUsername(), user2.getUsername()));
        Subscription user2ToUser1 = subscriptionRepository.saveAndFlush(new Subscription(user2.getUsername(), user1.getUsername()));
        Subscription user1ToUser3 = subscriptionRepository.saveAndFlush(new Subscription(user1.getUsername(), user3.getUsername()));
        Subscription user3ToUser1 = subscriptionRepository.saveAndFlush(new Subscription(user3.getUsername(), user1.getUsername()));
        subscriptionRepository.saveAndFlush(new Subscription(user4.getUsername(), user1.getUsername()));
        subscriptionRepository.saveAndFlush(new Subscription(user1.getUsername(), user5.getUsername()));
        Set<Friend> expected = Set.of(
                new Friend(user2.getUsername(), max(user2ToUser1.getCreationDate(), user1ToUser2.getCreationDate())),
                new Friend(user3.getUsername(), max(user3ToUser1.getCreationDate(), user1ToUser3.getCreationDate()))
        );
        Page<IFriend> allFriends = subscriptionRepository.findAllFriends(user1.getUsername(), PageRequest.of(0, 4));
        assertEquals(2, allFriends.getTotalElements());
        List<Friend> actual = allFriends.getContent().stream().map(iFriend -> new Friend(iFriend.getFriendUsername(), iFriend.getFriendshipStartDate())).toList();
        assertEquals(2, actual.size());
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @SafeVarargs
    private static <T extends Comparable<T>> T max(T... values) {
        return Collections.max(Arrays.asList(values));
    }
}