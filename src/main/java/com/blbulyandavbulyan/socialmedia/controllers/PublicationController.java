package com.blbulyandavbulyan.socialmedia.controllers;

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
    @PutMapping("/{publicationId}")
    public void update(@PathVariable Long publicationId, @Validated @RequestBody PublicationRequest publicationRequest, Principal principal){
    }
}
