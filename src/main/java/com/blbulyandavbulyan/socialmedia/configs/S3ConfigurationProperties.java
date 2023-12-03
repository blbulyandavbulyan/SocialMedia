package com.blbulyandavbulyan.socialmedia.configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties("socialmedia.s3")
public class S3ConfigurationProperties {
    private String s3Url;
    private String bucketName;
}
