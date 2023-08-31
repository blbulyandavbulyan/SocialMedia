package com.blbulyandavbulyan.socialmedia.services;

import com.blbulyandavbulyan.socialmedia.dtos.publications.PublicationCreatedResponse;
import com.blbulyandavbulyan.socialmedia.dtos.publications.PublicationRequest;
import com.blbulyandavbulyan.socialmedia.entites.File;
import com.blbulyandavbulyan.socialmedia.entites.Publication;
import com.blbulyandavbulyan.socialmedia.entites.User;
import com.blbulyandavbulyan.socialmedia.exceptions.publications.PublicationNotFoundException;
import com.blbulyandavbulyan.socialmedia.exceptions.publications.YouAreNotOwnThisPublicationException;
import com.blbulyandavbulyan.socialmedia.repositories.PublicationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public void delete(Long publicationId, String deleterName) {
        if(publicationRepository.existsById(publicationId)){
            if (publicationRepository.deleteByIdAndAuthorUsername(publicationId, deleterName) == 0)
                throw new YouAreNotOwnThisPublicationException("You can't delete this publication, because you don't own it");
        }
        else throw new PublicationNotFoundException("Publication with id " + publicationId + " not found!");
    }

    public void updateText(Long publicationId, String text, String updaterName) {
        String authorName = publicationRepository.findAuthorNameById(publicationId)
                .orElseThrow(() -> new PublicationNotFoundException("Publication with id " + publicationId + " not found!"));
        if (authorName.equals(updaterName)) publicationRepository.updateTextById(publicationId, text);
        else
            throw new YouAreNotOwnThisPublicationException("You don't own this publication, operation is not allowed!");
    }
    public void updateTitle(Long publicationId, String title){
        if(publicationRepository.updateTitleById(publicationId, title) == 0)
            throw new PublicationNotFoundException("Publication with id " + publicationId + " not found!");
    }
}
