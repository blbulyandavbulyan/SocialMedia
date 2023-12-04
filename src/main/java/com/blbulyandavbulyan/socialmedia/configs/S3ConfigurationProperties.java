package com.blbulyandavbulyan.socialmedia.configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties("social-media.s3")
@Profile("s3")
public class S3ConfigurationProperties {
    private String s3Url;
    private String bucketName;
}
