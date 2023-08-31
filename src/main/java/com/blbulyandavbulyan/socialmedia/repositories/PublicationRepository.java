package com.blbulyandavbulyan.socialmedia.repositories;

import com.blbulyandavbulyan.socialmedia.entites.Publication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface PublicationRepository extends JpaRepository<Publication, Long> {
    long deleteByIdAndAuthorUsername(Long id, String username);

    @Transactional
    @Modifying
    @Query("update Publication p set p.text = :text where p.id = :id")
    int updateTextById(@Param("id") Long id, @Param("text") String text);

    @Transactional
    @Modifying
    @Query("update Publication p set p.title = :title where p.id = :id")
    int updateTitleById(@Param("id") Long id, @Param("title") String title);

    @Query("select p.author.username from Publication p where p.id = :id")
    Optional<String> findAuthorNameById(Long id);
}