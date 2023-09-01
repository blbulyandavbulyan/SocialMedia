package com.blbulyandavbulyan.socialmedia.entites.keys;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class SubscriptionPK implements Serializable {
    private String subscriberUsername;
    private String targetUsername;
}
