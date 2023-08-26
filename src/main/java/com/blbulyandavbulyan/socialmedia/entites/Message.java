package com.blbulyandavbulyan.socialmedia.entites;

import jakarta.persistence.*;
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
    private String text;
    @ManyToOne
    @JoinColumn(name = "sender_username")
    private User sender;
    @ManyToOne
    @JoinColumn(name = "receiver_username")
    private User receiver;
    @CreationTimestamp
    private Instant sendingDate;
    private boolean read;
}