package com.blbulyandavbulyan.socialmedia.services;

import com.blbulyandavbulyan.socialmedia.entites.keys.SubscriptionPK;
import com.blbulyandavbulyan.socialmedia.repositories.SubscriptionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SubscriptionServiceTest {
    @Mock
    private SubscriptionRepository subscriptionRepository;
    @InjectMocks
    private SubscriptionService underTest;

    @Test
    void isFirstSubscriberForSecond() {
        String first = "david";
        String second = "andrey";
        Mockito.when(subscriptionRepository.existsById(new SubscriptionPK(first, second))).thenReturn(true);
        assertTrue(underTest.isFirstSubscriberForSecond(first, second));
        Mockito.verify(subscriptionRepository, Mockito.only()).existsById(new SubscriptionPK(first, second));
    }

    @Test
    void isFirstNotSubscriberForSecond() {
        String first = "david", second = "andrey";
        Mockito.when(subscriptionRepository.existsById(new SubscriptionPK(first, second))).thenReturn(false);
        assertFalse(underTest.isFirstSubscriberForSecond(first, second));
        Mockito.verify(subscriptionRepository, Mockito.only()).existsById(new SubscriptionPK(first, second));
    }

    @Test
    void unsubscribe() {
        String subscriber = "test";
        String target = "testtarget";
        assertDoesNotThrow(() -> underTest.unsubscribe(subscriber, target));
        Mockito.verify(subscriptionRepository, Mockito.only()).deleteById(new SubscriptionPK(subscriber, target));
    }
}