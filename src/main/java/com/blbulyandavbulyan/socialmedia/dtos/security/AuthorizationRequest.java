package com.blbulyandavbulyan.socialmedia.dtos.security;

import com.blbulyandavbulyan.socialmedia.annotations.validation.user.ValidRawPassword;
import com.blbulyandavbulyan.socialmedia.annotations.validation.user.ValidUserName;

/**
 * Объект, отправляемый на сервер для авторизации
 * @param username имя пользователя
 * @param password пароль
 */
public record AuthorizationRequest(@ValidUserName String username, @ValidRawPassword String password) {
}
