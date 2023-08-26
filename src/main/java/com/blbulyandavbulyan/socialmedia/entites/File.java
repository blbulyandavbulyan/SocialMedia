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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "file_name", nullable = false)
    private UUID fileName;
    @Column(name = "file_extension")
    private String fileExtension;
    @Column(name = "mime_type")
    private String mimeType;
    @CreationTimestamp
    private Instant loadingTime;
}