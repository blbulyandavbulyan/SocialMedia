package com.blbulyandavbulyan.socialmedia.controllers;

import com.blbulyandavbulyan.socialmedia.annotations.validation.publications.ValidPublicationText;
import com.blbulyandavbulyan.socialmedia.annotations.validation.publications.ValidPublicationTitle;
import com.blbulyandavbulyan.socialmedia.dtos.publications.PublicationCreatedResponse;
import com.blbulyandavbulyan.socialmedia.dtos.publications.PublicationRequest;
import com.blbulyandavbulyan.socialmedia.services.PublicationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/publications")
@AllArgsConstructor
public class PublicationController {
    private PublicationService publicationService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PublicationCreatedResponse create(@Validated @RequestBody PublicationRequest publicationRequest, Principal principal){
        return publicationService.create(publicationRequest, principal.getName());
    }
    @DeleteMapping("/{publicationId}")
    public void delete(@PathVariable Long publicationId, Principal principal){
        publicationService.delete(publicationId, principal.getName());
    }
    @PatchMapping("/{publicationId}/title")
    public void updateTitle(@PathVariable Long publicationId, @ValidPublicationTitle String title, Principal principal) {
        publicationService.updateTitle(publicationId, title, principal.getName());
    }
    @PatchMapping("/{publicationId}/text")
    public void updateText(@PathVariable Long publicationId, @ValidPublicationText String text, Principal principal) {
        publicationService.updateText(publicationId, text, principal.getName());
    }
}
