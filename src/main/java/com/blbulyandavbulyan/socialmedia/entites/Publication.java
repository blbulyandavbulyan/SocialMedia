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
    private String title;
    @NotBlank
    private String text;
    @ManyToOne
    @JoinColumn(name = "author_username")
    private User author;
    @CreationTimestamp
    @Column(name = "publication_date")
    private Instant publicationDate;
    @OneToMany
    @JoinTable(
            name = "publications_files",
            uniqueConstraints = @UniqueConstraint(columnNames = {"publication_id", "file_id"}),
            joinColumns = @JoinColumn(name = "publication_id"),
            inverseJoinColumns = @JoinColumn(name = "file_id")
    )
    private List<File> files;
}