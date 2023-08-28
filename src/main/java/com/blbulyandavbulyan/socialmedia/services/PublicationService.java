package com.blbulyandavbulyan.socialmedia.services;

import com.blbulyandavbulyan.socialmedia.dtos.publications.PublicationCreatedResponse;
import com.blbulyandavbulyan.socialmedia.dtos.publications.PublicationRequest;
import com.blbulyandavbulyan.socialmedia.entites.File;
import com.blbulyandavbulyan.socialmedia.entites.Publication;
import com.blbulyandavbulyan.socialmedia.entites.User;
import com.blbulyandavbulyan.socialmedia.repositories.PublicationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PublicationService {
    private PublicationRepository publicationRepository;
    private UserService userService;
    private FileService fileService;
    public PublicationCreatedResponse create(PublicationRequest publicationRequest, String publisherName) {
        User author = userService.findByUserName(publisherName).orElseThrow();
        List<File> attachedFiles = fileService.findAllById(publicationRequest.filesUUIDs());
        Publication publication = new Publication(publicationRequest.title(), publicationRequest.text(), author, attachedFiles);
        publicationRepository.save(publication);
        return new PublicationCreatedResponse(publication.getId(), publication.getPublicationDate());
    }

}
