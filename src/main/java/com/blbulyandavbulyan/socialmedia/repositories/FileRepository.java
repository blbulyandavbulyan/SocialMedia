package com.blbulyandavbulyan.socialmedia.repositories;

import com.blbulyandavbulyan.socialmedia.entites.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FileRepository extends JpaRepository<File, UUID> {
}