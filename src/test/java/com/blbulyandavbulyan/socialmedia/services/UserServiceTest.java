package com.blbulyandavbulyan.socialmedia.services;

import com.blbulyandavbulyan.socialmedia.entites.User;
import com.blbulyandavbulyan.socialmedia.repositories.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("load user by user name when user exists")
    void loadUserByUsernameWhenExists() {
        String username = "test";
        User user = new User();
        user.setUsername(username);
        Mockito.when(userRepository.findById(username)).thenReturn(Optional.of(user));
        UserDetails userDetails = userService.loadUserByUsername(username);
        Mockito.verify(userRepository, Mockito.only()).findById(username);
        assertSame(user, userDetails);
    }
    @Test
    @DisplayName("load user by user name when it doesn't exist")
    public void loadUserByUsernameWhenDoesNotExist(){
        String username = "test";
        Mockito.when(userRepository.findById(username)).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, ()->userService.loadUserByUsername(username));
        Mockito.verify(userRepository, Mockito.only()).findById(username);
    }
    @Test
    @DisplayName("save not existing user")
    public void saveNotExistingUser(){
        String password = "1234556";
        String username = "david";
        String email = "test@gmail.com";
        User user = new User(username, password, email);
        Mockito.when(userRepository.existsByUsername(username)).thenReturn(false);
        Mockito.when(userRepository.existsByEmail(email)).thenReturn(false);
        assertDoesNotThrow(()->userService.save(user));
        Mockito.verify(userRepository, Mockito.times(1)).existsByUsername(username);
        Mockito.verify(userRepository, Mockito.times(1)).existsByEmail(email);
        Mockito.verify(userRepository,Mockito.times(1)).save(user);
    }
}