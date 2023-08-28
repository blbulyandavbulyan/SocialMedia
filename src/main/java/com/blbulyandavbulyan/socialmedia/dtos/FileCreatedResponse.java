package com.blbulyandavbulyan.socialmedia.dtos;

import java.util.UUID;

/**
 * Ответ выдаваемый сервером, при успешной загрузки файла
 * @param fileUIID UUID загруженного файла
 */
public record FileCreatedResponse(UUID fileUIID) {
}
