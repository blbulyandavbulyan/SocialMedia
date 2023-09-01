package com.blbulyandavbulyan.socialmedia.services;

import com.blbulyandavbulyan.socialmedia.entites.Subscription;
import com.blbulyandavbulyan.socialmedia.entites.keys.SubscriptionPK;
import com.blbulyandavbulyan.socialmedia.exceptions.subscriptions.SubscriptionAlreadyExistsException;
import com.blbulyandavbulyan.socialmedia.exceptions.subscriptions.SubscriptionNotFoundException;
import com.blbulyandavbulyan.socialmedia.repositories.SubscriptionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SubscriptionService {
    private SubscriptionRepository subscriptionRepository;

    public boolean isFirstSubscriberForSecond(String first, String second) {
        return subscriptionRepository.existsById(new SubscriptionPK(first, second));
    }

    public void markSubscriptionAsViewed(String subscriber, String target) {
        if (subscriptionRepository.updateViewedBySubscriberAndTargetUsername(true, subscriber, target) < 1)
            throw new SubscriptionNotFoundException("The subscription from " + subscriber + " to " + target + " not found!");
    }

    public void create(String subscriber, String target) {
        Subscription subscription = new Subscription();
        subscription.setSubscriberUsername(subscriber);
        subscription.setTargetUsername(target);
        if (!subscriptionRepository.existsById(new SubscriptionPK(subscriber, target)))
            subscriptionRepository.save(subscription);
        else throw new SubscriptionAlreadyExistsException("You are already subscribed on " + target);
    }

    public void unsubscribe(String subscriber, String target) {
        subscriptionRepository.deleteById(new SubscriptionPK(subscriber, target));
    }
}
