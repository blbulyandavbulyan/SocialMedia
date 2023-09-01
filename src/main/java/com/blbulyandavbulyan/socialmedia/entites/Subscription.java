package com.blbulyandavbulyan.socialmedia.entites;

import com.blbulyandavbulyan.socialmedia.annotations.validation.ValidUserName;
import com.blbulyandavbulyan.socialmedia.entites.keys.SubscriptionPK;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "subscriptions")
@IdClass(SubscriptionPK.class)
public class Subscription {
    @Id
    @ValidUserName
    @Column(name = "subscriber_username", nullable = false)
    private String subscriberUsername;
    @Id
    @ValidUserName
    @Column(name = "target_user_name", nullable = false)
    private String targetUsername;
    @CreationTimestamp
    @Column(name = "creation_date", nullable = false)
    private Instant creationDate;
    @Column(name = "viewed")
    private Boolean viewed;
}