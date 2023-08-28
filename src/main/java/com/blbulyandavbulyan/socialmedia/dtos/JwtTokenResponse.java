package com.blbulyandavbulyan.socialmedia.dtos;

import jakarta.validation.constraints.NotBlank;

/**
 * Ответ при успешной авторизации
 * @param token JWT токен
 */
public record JwtTokenResponse(@NotBlank String token) {
}
