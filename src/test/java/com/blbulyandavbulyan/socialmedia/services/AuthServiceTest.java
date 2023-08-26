package com.blbulyandavbulyan.socialmedia.services;

import com.blbulyandavbulyan.socialmedia.entites.User;
import com.blbulyandavbulyan.socialmedia.exceptions.UserWithThisNameAlreadyExist;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Spy
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Mock
    private UserService userService;
    @InjectMocks
    private AuthService authService;
    @Test
    @DisplayName("register not existing user")
    public void registerNotExistingUser(){
        String username = "david";
        String password = "123234323";
        String email = "test@gmail.com";
        Mockito.when(userService.existByUsername(username)).thenReturn(false);
        assertDoesNotThrow(()->authService.registerUser(username, password, email));
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userService, Mockito.times(1)).existByUsername(username);
        Mockito.verify(userService, Mockito.times(1)).save(userCaptor.capture());
        User actualUser = userCaptor.getValue();
        assertTrue(passwordEncoder.matches(password, actualUser.getPassword()), "Passwords doesn't match!");
        assertEquals(username, actualUser.getUsername(), "User names are not equal!");
        assertEquals(email, actualUser.getEmail(), "Emails doesn't equal!");
    }
    @Test
    @DisplayName("register user with existing user name")
    public void registerUserWhenUserWithThisNameAlreadyExist(){
        String username = "david";
        String password = "123234323";
        String email = "test@gmail.com";
        Mockito.when(userService.existByUsername(username)).thenReturn(true);
        var actualException = assertThrows(UserWithThisNameAlreadyExist.class, ()-> authService.registerUser(username, password, email));
        assertEquals(HttpStatus.BAD_REQUEST, actualException.getHttpStatus());
        assertNotNull(actualException.getMessage());
        Mockito.verify(userService, Mockito.times(1)).existByUsername(username);
        Mockito.verify(userService, Mockito.never()).save(any());
    }
}