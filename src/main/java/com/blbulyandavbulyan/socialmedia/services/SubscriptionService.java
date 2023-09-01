package com.blbulyandavbulyan.socialmedia.services;

import com.blbulyandavbulyan.socialmedia.entites.Subscription;
import com.blbulyandavbulyan.socialmedia.entites.keys.SubscriptionPK;
import com.blbulyandavbulyan.socialmedia.repositories.SubscriptionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SubscriptionService {
    private SubscriptionRepository subscriptionRepository;

    public void subscribe(String subscriber, String target) {
        Subscription subscription = new Subscription();
        subscription.setSubscriberUsername(subscriber);
        subscription.setTargetUsername(target);
        if (!subscriptionRepository.existsBySubscriberUsernameAndTargetUsername(subscriber, target))
            subscriptionRepository.save(subscription);
        else throw new RuntimeException();
    }

    public boolean isFirstSubscriberForSecond(String first, String second) {
        return subscriptionRepository.existsBySubscriberUsernameAndTargetUsername(first, second);
    }

    public void markSubscriptionAsViewed(String friendshipAcceptor, String friendshipSender) {
        if (subscriptionRepository.updateViewedBySubscriberAndTargetUsername(true, friendshipSender, friendshipAcceptor) < 1)
            throw new RuntimeException();// TODO: 01.09.2023 Бросить исключение говорящее о том, что такого запроса не было
    }

    public void create(String subscriber, String target) {
        Subscription subscription = new Subscription();
        subscription.setSubscriberUsername(subscriber);
        subscription.setTargetUsername(target);
        if (!subscriptionRepository.existsById(new SubscriptionPK(subscriber, target)))
            subscriptionRepository.save(subscription);
        else throw new RuntimeException();
    }
}
