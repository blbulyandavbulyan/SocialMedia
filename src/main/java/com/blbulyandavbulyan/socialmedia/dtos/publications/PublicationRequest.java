package com.blbulyandavbulyan.socialmedia.dtos.publications;

import java.util.List;
import java.util.UUID;

/**
 * Предоставляет запрос для создания публикации
 * @param title заголовок публикации
 * @param text текст публикации
 * @param filesUUIDs UUID прикрепляемых файлов
 */
public record PublicationRequest(String title, String text, List<UUID> filesUUIDs) {
}
