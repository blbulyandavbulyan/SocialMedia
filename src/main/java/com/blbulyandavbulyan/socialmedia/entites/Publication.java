package com.blbulyandavbulyan.socialmedia.entites;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "publications")
public class Publication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "publication_id")
    private Long id;
    @NotBlank
    @Column(name = "title", nullable = false)
    private String title;
    @NotBlank
    @Column(name = "text", nullable = false, columnDefinition = "TEXT")
    private String text;
    @ManyToOne
    @JoinColumn(name = "author_username", nullable = false)
    private User author;
    @CreationTimestamp
    @Column(name = "publication_date", nullable = false)
    private Instant publicationDate;
    @OneToMany
    @JoinTable(
            name = "publications_files",
            uniqueConstraints = @UniqueConstraint(columnNames = {"file_id", "publication_id"}),
            joinColumns = @JoinColumn(name = "publication_id"),
            inverseJoinColumns = @JoinColumn(name = "file_id")
    )
    private List<File> files;
}