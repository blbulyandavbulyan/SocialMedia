package com.blbulyandavbulyan.socialmedia.dtos;

import lombok.Getter;

import java.time.Instant;

/**
 * DTO для предоставления информации о друге
 * @param friendUsername    имя пользователя друга
 * @param friendshipStartDate дата и время начала дружбы
 */
public record Friend(@Getter String friendUsername, @Getter Instant friendshipStartDate) implements IFriend {

}
