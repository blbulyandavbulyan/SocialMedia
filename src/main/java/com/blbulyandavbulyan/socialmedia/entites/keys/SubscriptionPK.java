package com.blbulyandavbulyan.socialmedia.entites.keys;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class SubscriptionPK implements Serializable {
    private String subscriberUsername;
    private String targetUsername;
}
