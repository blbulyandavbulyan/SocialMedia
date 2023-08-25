package com.blbulyandavbulyan.socialmedia.dtos;

import jakarta.validation.constraints.NotBlank;

public record JwtTokenResponse(@NotBlank String token) {
}
