package com.blbulyandavbulyan.socialmedia.services;

import com.blbulyandavbulyan.socialmedia.exceptions.publications.YouAreNotOwnThisPublicationException;
import com.blbulyandavbulyan.socialmedia.repositories.PublicationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        assertDoesNotThrow(()->publicationService.delete(publicationId, publisherName));
        Mockito.verify(publicationRepository, Mockito.times(1)).existsById(publicationId);
        Mockito.verify(publicationRepository, Mockito.times(1)).deleteByIdAndAuthorUsername(publicationId, publisherName);
    }

    @Test
    @DisplayName("delete publication when it exist and deleter is not its owner")
    void deletePublicationWhenDeleterIsNotItsOwner() {
        Long publicationId = 1L;
        String publisherName = "david";
        Mockito.when(publicationRepository.existsById(publicationId)).thenReturn(true);
        Mockito.when(publicationRepository.deleteByIdAndAuthorUsername(publicationId, publisherName)).thenReturn(1L);
        assertThrows(YouAreNotOwnThisPublicationException.class, ()->publicationService.delete(publicationId, publisherName));
        Mockito.verify(publicationRepository, Mockito.times(1)).existsById(publicationId);
        Mockito.verify(publicationRepository, Mockito.times(1)).deleteByIdAndAuthorUsername(publicationId, publisherName);
    }
}
