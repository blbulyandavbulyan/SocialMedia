package com.blbulyandavbulyan.socialmedia.services;

import com.blbulyandavbulyan.socialmedia.entites.Subscription;
import com.blbulyandavbulyan.socialmedia.entites.keys.SubscriptionPK;
import com.blbulyandavbulyan.socialmedia.exceptions.subscriptions.AttemptToSubscribeForYourselfException;
import com.blbulyandavbulyan.socialmedia.exceptions.subscriptions.SubscriptionAlreadyExistsException;
import com.blbulyandavbulyan.socialmedia.exceptions.subscriptions.SubscriptionNotFoundException;
import com.blbulyandavbulyan.socialmedia.repositories.SubscriptionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

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

    @Test
    void createWhenSubscriptionDoesNotExist() {
        String subscriber = "andrey";
        String target = "david";
        Mockito.when(subscriptionRepository.existsById(new SubscriptionPK(subscriber, target))).thenReturn(false);
        assertDoesNotThrow(() -> underTest.create(subscriber, target));
        Mockito.verify(subscriptionRepository, Mockito.times(1)).existsById(new SubscriptionPK(subscriber, target));
        ArgumentCaptor<Subscription> subscriptionArgumentCaptor = ArgumentCaptor.forClass(Subscription.class);
        Mockito.verify(subscriptionRepository, Mockito.times(1)).save(subscriptionArgumentCaptor.capture());
        Subscription actualSubscription = subscriptionArgumentCaptor.getValue();
        assertEquals(subscriber, actualSubscription.getSubscriberUsername());
        assertEquals(target, actualSubscription.getTargetUsername());
    }

    @Test
    void createSubscriptionWhenItAlreadyExists() {
        String subscriber = "andrey";
        String target = "david";
        SubscriptionPK id = new SubscriptionPK(subscriber, target);
        Mockito.when(subscriptionRepository.existsById(id)).thenReturn(true);
        assertThrows(SubscriptionAlreadyExistsException.class, () -> underTest.create(subscriber, target));
        Mockito.verify(subscriptionRepository, Mockito.only()).existsById(id);
    }

    @Test
    void createSubscriptionForYourself() {
        String subscriberAndTarget = "david";
        assertThrows(AttemptToSubscribeForYourselfException.class, () -> underTest.create(subscriberAndTarget, subscriberAndTarget));
        Mockito.verify(subscriptionRepository, Mockito.never()).save(any());
    }

    @Test
    void markSubscriptionAsViewedIfItExists() {
        String subscriber = "anatoly";
        String target = "evgeney";
        Mockito.when(subscriptionRepository.updateViewedBySubscriberAndTargetUsername(true, subscriber, target)).thenReturn(1);
        assertDoesNotThrow(() -> underTest.markSubscriptionAsViewed(subscriber, target));
        Mockito.verify(subscriptionRepository, Mockito.only()).updateViewedBySubscriberAndTargetUsername(true, subscriber, target);
    }

    @Test
    void markSubscriptionAsViewedIfItNotExists() {
        String subscriber = "anatoly";
        String target = "evgeney";
        Mockito.when(subscriptionRepository.updateViewedBySubscriberAndTargetUsername(true, subscriber, target)).thenReturn(0);
        assertThrows(SubscriptionNotFoundException.class, () -> underTest.markSubscriptionAsViewed(subscriber, target));
        Mockito.verify(subscriptionRepository, Mockito.only()).updateViewedBySubscriberAndTargetUsername(true, subscriber, target);
    }
}