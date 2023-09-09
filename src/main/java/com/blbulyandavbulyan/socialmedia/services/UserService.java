package com.blbulyandavbulyan.socialmedia.services;


import com.blbulyandavbulyan.socialmedia.entites.User;
import com.blbulyandavbulyan.socialmedia.exceptions.user.UserWithThisEmailAlreadyExists;
import com.blbulyandavbulyan.socialmedia.exceptions.user.UserWithThisNameAlreadyExist;
import com.blbulyandavbulyan.socialmedia.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findById(username).orElseThrow(() -> new UsernameNotFoundException("User with name " + username + " not found!"));
    }
    public void save(User user) {
        if (userRepository.existsByUsername(user.getUsername()))
            throw new UserWithThisNameAlreadyExist(user.getUsername());
        if(userRepository.existsByEmail(user.getEmail()))
            throw new UserWithThisEmailAlreadyExists(user.getEmail());
        userRepository.save(user);
    }

    public Optional<User> findByUserName(String username) {
        return userRepository.findById(username);
    }
}
