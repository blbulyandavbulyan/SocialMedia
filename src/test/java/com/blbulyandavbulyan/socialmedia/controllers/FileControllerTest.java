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
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.util.Optional;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
//import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.multipart;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.multipart;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class FileControllerTest {
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
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "file", "image.jpg", "image/jpeg",
                this.getClass().getResourceAsStream("/testimages/test_image.jpg")
        );
        HttpHeaders httpHeaders = new HttpHeaders();
        User user = new User("david", "sssfsfs", "test@gmail.com");
        Mockito.when(fileConfiguration.getPath()).thenReturn(Files.createTempDirectory("socialMediaTmpDir"));
        Mockito.when(fileConfiguration.isValidExtension(".jpg")).thenReturn(true);
        Mockito.when(fileConfiguration.isValidMimeType(mockMultipartFile.getContentType())).thenReturn(true);
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
}
