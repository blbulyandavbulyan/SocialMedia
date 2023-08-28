package com.blbulyandavbulyan.socialmedia.dtos;

import com.blbulyandavbulyan.socialmedia.annotations.validation.ValidRawPassword;
import com.blbulyandavbulyan.socialmedia.annotations.validation.ValidUserEmail;
import com.blbulyandavbulyan.socialmedia.annotations.validation.ValidUserName;

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
