package com.blbulyandavbulyan.socialmedia.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;

import java.net.URI;

@Configuration
@RequiredArgsConstructor
public class S3Config {
    private final S3ConfigurationProperties s3ConfigurationProperties;

    @Bean
    public S3Client s3Client() {
        S3ClientBuilder builder = S3Client.builder();
        builder.region(Region.AP_EAST_1);
        String s3Url = s3ConfigurationProperties.getS3Url();
        if (!s3Url.equals("default"))
            builder.endpointOverride(URI.create(s3Url));
        return builder.build();
    }
}
