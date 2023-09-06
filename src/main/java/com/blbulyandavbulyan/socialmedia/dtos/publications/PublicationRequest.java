package com.blbulyandavbulyan.socialmedia.dtos.publications;

import com.blbulyandavbulyan.socialmedia.annotations.validation.publications.ValidPublicationText;
import com.blbulyandavbulyan.socialmedia.annotations.validation.publications.ValidPublicationTitle;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

/**
 * Предоставляет запрос для создания публикации
 * @param title заголовок публикации
 * @param text текст публикации
 * @param filesUUIDs UUID прикрепляемых файлов
 */
public record PublicationRequest(@ValidPublicationTitle String title, @ValidPublicationText String text, @NotNull List<@NotNull UUID> filesUUIDs) {
}
