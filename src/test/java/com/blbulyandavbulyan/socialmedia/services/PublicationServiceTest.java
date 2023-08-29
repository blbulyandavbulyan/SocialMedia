package com.blbulyandavbulyan.socialmedia.services;

import com.blbulyandavbulyan.socialmedia.dtos.publications.PublicationRequest;
import com.blbulyandavbulyan.socialmedia.entites.File;
import com.blbulyandavbulyan.socialmedia.entites.Publication;
import com.blbulyandavbulyan.socialmedia.entites.User;
import com.blbulyandavbulyan.socialmedia.exceptions.publications.PublicationNotFoundException;
import com.blbulyandavbulyan.socialmedia.exceptions.publications.YouAreNotOwnThisPublicationException;
import com.blbulyandavbulyan.socialmedia.repositories.PublicationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class PublicationServiceTest {
    @Mock
    private PublicationRepository publicationRepository;
    @Mock
    private UserService userService;
    @Mock
    private FileService fileService;
    @InjectMocks
    private PublicationService publicationService;

    @Test
    @DisplayName("delete publication when exists and deleter is its owner")
    void deletePublicationWhenExistsAndDeleterIsItsOwner() {
        Long publicationId = 1L;
        String publisherName = "david";
        Mockito.when(publicationRepository.existsById(publicationId)).thenReturn(true);
        Mockito.when(publicationRepository.deleteByIdAndAuthorUsername(publicationId, publisherName)).thenReturn(1L);
        assertDoesNotThrow(() -> publicationService.delete(publicationId, publisherName));
        Mockito.verify(publicationRepository, Mockito.times(1)).existsById(publicationId);
        Mockito.verify(publicationRepository, Mockito.times(1)).deleteByIdAndAuthorUsername(publicationId, publisherName);
    }

    @Test
    @DisplayName("delete publication when it exist and deleter is not its owner")
    void deletePublicationWhenDeleterIsNotItsOwner() {
        Long publicationId = 1L;
        String publisherName = "david";
        Mockito.when(publicationRepository.existsById(publicationId)).thenReturn(true);
        Mockito.when(publicationRepository.deleteByIdAndAuthorUsername(publicationId, publisherName)).thenReturn(0L);
        assertThrows(YouAreNotOwnThisPublicationException.class, () -> publicationService.delete(publicationId, publisherName));
        Mockito.verify(publicationRepository, Mockito.times(1)).existsById(publicationId);
        Mockito.verify(publicationRepository, Mockito.times(1)).deleteByIdAndAuthorUsername(publicationId, publisherName);
    }

    @Test
    @DisplayName("delete publication when it doesn't exists")
    public void deletePublicationWhenDoesNotExist() {
        Long publicationId = 1L;
        Mockito.when(publicationRepository.existsById(publicationId)).thenReturn(false);
        assertThrows(PublicationNotFoundException.class, () -> publicationService.delete(publicationId, "testpublisher"));
        Mockito.verify(publicationRepository, Mockito.only()).existsById(publicationId);
    }

    @Test
    @DisplayName("normal create publication")
    void normalCreatePublication() {
        String username = "test";
        String expectedTitle = "Test publication";
        String expectedText = "Test text";
        Long expectedPublicationId = 1L;
        Instant expectedPublicationDate = Instant.now();
        User expectedAuthor = new User();
        expectedAuthor.setUsername(username);

        Mockito.when(userService.findByUserName(username)).thenReturn(Optional.of(expectedAuthor));
        List<UUID> filesUUID = List.of(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
        List<File> expectedFiles = new ArrayList<>();
        Mockito.when(fileService.findAllById(filesUUID)).thenReturn(expectedFiles);
        Mockito.when(publicationRepository.save(any())).then(invocation -> {
            Publication publication = (Publication) invocation.getArguments()[0];
            publication.setId(expectedPublicationId);
            publication.setPublicationDate(expectedPublicationDate);
            return publication;
        });
        var publicationCreatedRequest = new PublicationRequest(expectedTitle, expectedText, filesUUID);
        var actualResult = publicationService.create(publicationCreatedRequest, username);
        Publication savedPublication;
        {
            var publicationCaptor = ArgumentCaptor.forClass(Publication.class);
            Mockito.verify(publicationRepository, Mockito.times(1)).save(publicationCaptor.capture());
            savedPublication = publicationCaptor.getValue();
        }
        Mockito.verify(userService, Mockito.only()).findByUserName(username);
        Mockito.verify(fileService, Mockito.only()).findAllById(filesUUID);
        //проверка сохранённой публикации
        assertEquals(expectedTitle, savedPublication.getTitle());
        assertEquals(expectedText, savedPublication.getText());
        assertSame(expectedFiles, savedPublication.getFiles());
        assertSame(expectedAuthor, savedPublication.getAuthor());
        //проверка резульатата, который нам вернул метод
        assertEquals(expectedPublicationId, actualResult.publicationId());
        assertEquals(expectedPublicationDate, actualResult.publicationDate());
    }
}
