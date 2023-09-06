package com.blbulyandavbulyan.socialmedia.entites;

import com.blbulyandavbulyan.socialmedia.annotations.validation.ValidUserName;
import com.blbulyandavbulyan.socialmedia.entites.keys.SubscriptionPK;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "subscriptions")
@IdClass(SubscriptionPK.class)
@NoArgsConstructor
public class Subscription {
    @Id
    @ValidUserName
    @Column(name = "subscriber_username", nullable = false)
    private String subscriberUsername;
    @Id
    @ValidUserName
    @Column(name = "target_username", nullable = false)
    private String targetUsername;
    @CreationTimestamp
    @Column(name = "creation_date", nullable = false)
    private Instant creationDate;
    @Column(name = "viewed", nullable = false)
    private boolean viewed;

    public Subscription(String subscriberUsername, String targetUsername) {
        this.subscriberUsername = subscriberUsername;
        this.targetUsername = targetUsername;
    }
}