package com.blbulyandavbulyan.socialmedia.dtos.security;

import com.blbulyandavbulyan.socialmedia.annotations.validation.user.ValidRawPassword;
import com.blbulyandavbulyan.socialmedia.annotations.validation.user.ValidUserEmail;
import com.blbulyandavbulyan.socialmedia.annotations.validation.user.ValidUserName;

/**
 * Запрос регистрации
 * @param username имя пользователя
 * @param password пароль
 * @param email email
 */
public record RegistrationRequest (
        @ValidUserName String username,
        @ValidRawPassword String password,
        @ValidUserEmail String email
){
}
