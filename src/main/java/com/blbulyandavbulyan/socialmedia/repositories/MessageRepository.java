package com.blbulyandavbulyan.socialmedia.repositories;

import com.blbulyandavbulyan.socialmedia.dtos.messages.MessageResponse;
import com.blbulyandavbulyan.socialmedia.entites.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
    Page<MessageResponse> findByReceiverUsernameAndSenderUsernameOrderBySendingDateDesc(String receiverName, String senderName, Pageable pageable);

    @Transactional
    @Modifying
    @Query("update Message m set m.read = :read where m.id = :id")
    int updateReadById(@Param("id") Long id, @Param("read") boolean read);
}