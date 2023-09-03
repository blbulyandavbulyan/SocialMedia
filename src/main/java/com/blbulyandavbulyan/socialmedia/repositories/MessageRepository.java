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

public interface MessageRepository extends JpaRepository<Message, Long> {
    Page<MessageResponse> findByReceiverUsernameAndSenderUsernameOrderBySendingDateDesc(String receiverName, String senderName, Pageable pageable);

    @Transactional
    @Modifying
    @Query("update Message m set m.read = :read where m.id = :id")
    int updateReadById(@Param("id") Long id, @Param("read") boolean read);
}