package com.blbulyandavbulyan.socialmedia.controllers;

import com.blbulyandavbulyan.socialmedia.configs.FileConfigurationProperties;
import com.blbulyandavbulyan.socialmedia.entites.User;
import com.blbulyandavbulyan.socialmedia.repositories.FileRepository;
import com.blbulyandavbulyan.socialmedia.repositories.UserRepository;
import com.blbulyandavbulyan.socialmedia.services.FileService;
import com.blbulyandavbulyan.socialmedia.utils.JWTTokenUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.InputStream;
import java.net.URL;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.multipart;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class FileControllerTest {
    private final String testFileName = "/testimages/test_image.jpg";
    @Autowired
    private MockMvc mockMvc;
    @SpyBean
    private FileService fileService;
    @MockBean
    private FileRepository fileRepository;
    @Autowired
    private JWTTokenUtils jwtTokenUtils;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private FileConfigurationProperties fileConfiguration;

    @Test
    @DisplayName("upload file with authorized user")
    public void uploadFileIfYouAreAuthorizedUser() throws Exception {
        // TODO: 06.09.2023 Подумать, может тут стоит всё таки замокировать FileService
        String contentType = "image/jpeg";
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "file", "image.jpg", contentType,
                this.getClass().getResourceAsStream(testFileName)
        );
        HttpHeaders httpHeaders = new HttpHeaders();
        User user = new User("david", "sssfsfs", "test@gmail.com");
//        Mockito.when(fileConfiguration.getPath()).thenReturn(Files.createTempDirectory("socialMediaTmpDir"));
        Mockito.when(fileConfiguration.getValidExtensionForMimeType(contentType)).thenReturn(Optional.of(Set.of(".jpg")));
        Mockito.when(userRepository.findById(user.getUsername())).thenReturn(Optional.of(user));
        String jwtToken = jwtTokenUtils.generateToken(user.getUsername(), user.getAuthorities());
        httpHeaders.add("Authorization", "Bearer " + jwtToken);
        mockMvc.perform(multipart("/api/v1/files").file(mockMultipartFile).headers(httpHeaders))
                .andExpect(status().isCreated())
                .andDo(document("normal-uploading-file", resourceDetails().description("Upload file to server").tag("files"),
                        requestParts(partWithName("file").description("Uploading file")),
                        responseFields(fieldWithPath("fileUIID").type(JsonFieldType.STRING).description("The UUID of the uploaded file"))
                ));
        Mockito.verify(fileService, Mockito.times(1)).save(mockMultipartFile, user.getUsername());
    }

    @Test
    @DisplayName("normal get uploaded file")
    void getUploadedFile() throws Exception {
        UUID fileUIID = UUID.randomUUID();
        String contentType = "image/jpeg";
        URL resource = this.getClass().getResource(testFileName);
        assertNotNull(resource, "Test resource not found, please check if it exists");
        FileService.FoundFile foundFile = new FileService.FoundFile("test.jpg", contentType, new UrlResource(resource));
        Mockito.doReturn(foundFile).when(fileService).getFile(fileUIID);
        User user = new User("david", "sssfsfs", "test@gmail.com");
        String jwtToken = jwtTokenUtils.generateToken(user.getUsername(), user.getAuthorities());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + jwtToken);
        try (InputStream inputStream = resource.openStream()) {
            byte[] expectedContent = inputStream.readAllBytes();
            mockMvc.perform(get("/api/v1/files/{fileUUID}", fileUIID).headers(httpHeaders))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(contentType))
                    .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + foundFile.fileName() + "\""))
                    .andExpect(header().string(HttpHeaders.CONTENT_LENGTH, Integer.toString(expectedContent.length)))
                    .andExpect(content().bytes(expectedContent))
                    .andDo(document("normal-get-file",
                            resourceDetails().description("Get uploaded file by UUID").tag("files"),
                            pathParameters(parameterWithName("fileUUID").description("UUID of uploaded file, which you want to get")),
                            responseHeaders(
                                    headerWithName(HttpHeaders.CONTENT_TYPE).description("Type of your file"),
                                    headerWithName(HttpHeaders.CONTENT_LENGTH).description("File size in bytes"),
                                    headerWithName(HttpHeaders.CONTENT_DISPOSITION).description("This header will contain original file name, with this file name file was uploaded")
                            )
                    ));
        }
    }
}
