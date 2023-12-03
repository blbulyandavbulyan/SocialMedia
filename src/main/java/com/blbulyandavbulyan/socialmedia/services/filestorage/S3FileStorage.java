package com.blbulyandavbulyan.socialmedia.services.filestorage;

import com.blbulyandavbulyan.socialmedia.configs.S3ConfigurationProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Profile("s3")
public class S3FileStorage implements FileStorage {
    private final S3Client s3Client;
    private final S3ConfigurationProperties s3ConfigurationProperties;

    @Override
    public void save(MultipartFile multipartFile, UUID fileUUID) {
        try {
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(s3ConfigurationProperties.getBucketName())
                            .key(fileUUID.toString())
                            .contentType(multipartFile.getContentType()).build(),
                    RequestBody.fromBytes(multipartFile.getBytes())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Resource getFile(UUID fileUIID) {
        ResponseInputStream<GetObjectResponse> object = s3Client.getObject(GetObjectRequest.builder().bucket(s3ConfigurationProperties.getBucketName()).key(fileUIID.toString()).build());
        try {
            return new ByteArrayResource(object.readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
