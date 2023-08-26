package com.blbulyandavbulyan.socialmedia.repositories;

import com.blbulyandavbulyan.socialmedia.entites.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByEmail(String email);
}