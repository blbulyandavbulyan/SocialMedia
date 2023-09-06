package com.blbulyandavbulyan.socialmedia.dtos.subcriptions;

import java.time.Instant;

public record SubscriptionResponse(String subscriberUsername, Instant creationDate, boolean viewed) {
}
