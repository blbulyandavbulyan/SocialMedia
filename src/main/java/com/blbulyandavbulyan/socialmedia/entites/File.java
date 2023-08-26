package com.blbulyandavbulyan.socialmedia.entites;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "files")
public class File {
    @Id
    @Column(name = "saved_file_name", nullable = false)
    private UUID savedFileName;
    @ManyToOne
    @JoinColumn(name = "uploader_username", nullable = false)
    private User uploader;
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "real_file_name", nullable = false)
    private String realFileName;
    @Column(name = "file_extension", nullable = false)
    private String fileExtension;
    @Column(name = "mime_type", nullable = false)
    private String mimeType;
    @CreationTimestamp
    @Column(name = "loading_time", nullable = false)
    private Instant loadingTime;
}