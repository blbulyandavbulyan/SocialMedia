package com.blbulyandavbulyan.socialmedia.dtos.publications;

import java.util.List;
import java.util.UUID;

public record PublicationResponse(Long publicationId, String title, String text, List<UUID> attachedFilesUUIDs) {
}
