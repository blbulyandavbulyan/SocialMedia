package com.blbulyandavbulyan.socialmedia.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class FriendshipServiceTest {
    @Mock
    private SubscriptionService subscriptionService;
    @InjectMocks
    private FriendshipService underTest;

    @Test
    void areTheyFriendsShouldReturnTrueIfExistsSubscriptionToEachOther() {
        String first = "david";
        String second = "andrey";
        Mockito.when(subscriptionService.isFirstSubscriberForSecond(first, second)).thenReturn(true);
        Mockito.when(subscriptionService.isFirstSubscriberForSecond(second, first)).thenReturn(true);
        assertTrue(underTest.areTheyFriends(first, second));
        assertTrue(underTest.areTheyFriends(second, first));
    }

    @Test
    void areTheyFriendsShouldReturnFalseIfFirstIsNotSubscriberForSecond() {
        String first = "david";
        String second = "andrey";
        Mockito.when(subscriptionService.isFirstSubscriberForSecond(first, second)).thenReturn(false);
        Mockito.when(subscriptionService.isFirstSubscriberForSecond(second, first)).thenReturn(true);
        assertFalse(underTest.areTheyFriends(first, second));
        assertFalse(underTest.areTheyFriends(second, first));
    }

    @Test
    void areTheyFriendsShouldReturnFalseIfSecondIsNotSubscriberForFirst() {
        String first = "david";
        String second = "andrey";
        Mockito.when(subscriptionService.isFirstSubscriberForSecond(first, second)).thenReturn(true);
        Mockito.when(subscriptionService.isFirstSubscriberForSecond(second, first)).thenReturn(false);
        assertFalse(underTest.areTheyFriends(first, second));
        assertFalse(underTest.areTheyFriends(second, first));
    }

    @Test
    void areTheyFriendsShouldReturnFalseIfFirstNotSubscriberForSecondAndSecondNotSubscriberForFirst() {
        String first = "david";
        String second = "andrey";
        Mockito.when(subscriptionService.isFirstSubscriberForSecond(first, second)).thenReturn(false);
        Mockito.when(subscriptionService.isFirstSubscriberForSecond(second, first)).thenReturn(false);
        assertFalse(underTest.areTheyFriends(first, second));
        assertFalse(underTest.areTheyFriends(second, first));
    }
}