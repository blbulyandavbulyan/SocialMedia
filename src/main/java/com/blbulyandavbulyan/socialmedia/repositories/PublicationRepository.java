package com.blbulyandavbulyan.socialmedia.repositories;

import com.blbulyandavbulyan.socialmedia.entites.Publication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface PublicationRepository extends JpaRepository<Publication, Long> {
    long deleteByIdAndAuthorUsername(Long id, String username);

    @Transactional
    @Modifying
    @Query("update Publication p set p.text = :text where p.id = :id")
    int updateTextById(@Param("text") String text, @Param("id") Long id);

    @Transactional
    @Modifying
    @Query("update Publication p set p.title = :title where p.id = :id")
    int updateTitleById(@Param("title") String title, @Param("id") Long id);

}