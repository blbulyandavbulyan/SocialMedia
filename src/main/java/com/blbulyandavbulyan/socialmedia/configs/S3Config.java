package com.blbulyandavbulyan.socialmedia.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;

import java.net.URI;

@Configuration
@RequiredArgsConstructor
@Profile("s3")
public class S3Config {
    private final S3ConfigurationProperties s3ConfigurationProperties;
    @Value("${socialmedia.s3.accessKeyId}")
    private String accessKeyId;
    @Value("${socialmedia.s3.secretAccessKey}")
    private String secretAccessKey;
    @Bean
    public S3Client s3Client() {
        S3ClientBuilder builder = S3Client.builder();
        builder.region(Region.AP_EAST_1);
        String s3Url = s3ConfigurationProperties.getS3Url();
        if (!s3Url.equals("default")) {
            builder.endpointOverride(URI.create(s3Url));
            builder.forcePathStyle(true);
        }
        builder.credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKeyId, secretAccessKey)));
        return builder.build();
    }
}
