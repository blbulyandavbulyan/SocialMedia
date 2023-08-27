package com.blbulyandavbulyan.socialmedia.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExtensionResolverTest {
    private final ExtensionResolver extensionResolver = new ExtensionResolver();

    @Test
    @DisplayName("get file extension when extension exists")
    public void getFileExtensionWhenExtensionExists() {
        String fileName = "test.jpg";
        String expected = ".jpg";
        Optional<String> fileExtensionOptional = extensionResolver.getFileExtension(fileName);
        assertTrue(fileExtensionOptional.isPresent());
        assertEquals(expected, fileExtensionOptional.get());

        fileName = "test.jpg.jpeg";
        expected = ".jpeg";
        fileExtensionOptional = extensionResolver.getFileExtension(fileName);
        assertTrue(fileExtensionOptional.isPresent());
        assertEquals(expected, fileExtensionOptional.get());

        fileName = "test.g.e";
        expected = ".e";
        fileExtensionOptional = extensionResolver.getFileExtension(fileName);
        assertTrue(fileExtensionOptional.isPresent());
        assertEquals(expected, fileExtensionOptional.get());

        fileName = "a.pdf";
        expected = ".pdf";
        fileExtensionOptional = extensionResolver.getFileExtension(fileName);
        assertTrue(fileExtensionOptional.isPresent());
        assertEquals(expected, fileExtensionOptional.get());

        fileName = ".jpe";
        expected = ".jpe";

        fileExtensionOptional = extensionResolver.getFileExtension(fileName);
        assertTrue(fileExtensionOptional.isPresent());
        assertEquals(expected, fileExtensionOptional.get());
    }
    @Test
    @DisplayName("get file extension when it doesn't exist")
    public void getFileExtensionWhenItDoesNotExist(){
        assertTrue(extensionResolver.getFileExtension("test").isEmpty());
        assertTrue(extensionResolver.getFileExtension("test.").isEmpty());
    }
}
