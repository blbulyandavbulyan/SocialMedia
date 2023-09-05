package com.blbulyandavbulyan.socialmedia.services;

public interface IFriendService {
    /**
     * Проверяет друзья ли пользователи
     *
     * @param username1 имя первого пользователя
     * @param username2 имя второго пользователя
     * @return true если пользователи друзья
     */
    boolean areTheyFriends(String username1, String username2);
}
