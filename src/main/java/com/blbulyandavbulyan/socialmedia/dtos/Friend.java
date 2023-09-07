package com.blbulyandavbulyan.socialmedia.dtos;

import java.time.Instant;

/**
 * DTO для предоставления информации о друге
 *
 * @param friendUsername    имя пользователя друга
 * @param friendShipStarted дата и время начала дружбы
 */
public record Friend(String friendUsername, Instant friendShipStarted) {
}
