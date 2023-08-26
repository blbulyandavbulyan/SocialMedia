package com.blbulyandavbulyan.socialmedia.entites;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long id;
    @NotBlank
    @Column(name = "text", nullable = false, columnDefinition = "TEXT")
    private String text;
    @ManyToOne
    @JoinColumn(name = "sender_username", nullable = false)
    private User sender;
    @ManyToOne
    @JoinColumn(name = "receiver_username", nullable = false)
    private User receiver;
    @CreationTimestamp
    @Column(name = "sending_date", nullable = false)
    private Instant sendingDate;
    @Column(name = "read", nullable = false)
    private boolean read;
}