package com.blbulyandavbulyan.socialmedia.repositories;

import com.blbulyandavbulyan.socialmedia.dtos.messages.MessageResponse;
import com.blbulyandavbulyan.socialmedia.entites.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query("""
            SELECT m.id as id, m.receiver.username as receiverUsername, m.sender.username as senderUsername, m.text as text, m.sendingDate as sendingDate, m.read as read
            FROM  Message m WHERE (m.sender.username = :firstUsername AND m.receiver.username = :secondUsername)
             OR (m.sender.username = :secondUsername AND m.receiver.username = :firstUsername) ORDER BY m.sendingDate DESC
            """)
    Page<MessageResponse> getMessagesBetweenUsers(@Param("firstUsername") String first, @Param("secondUsername") String second, Pageable pageable);

    @Transactional
    @Modifying
    @Query("update Message m set m.read = :read where m.id = :id")
    int updateReadById(@Param("id") Long id, @Param("read") boolean read);

    @Query("select m.receiver.username from Message m where m.id = :id")
    Optional<String> findReceiverUsernameById(@Param("id") Long id);
}