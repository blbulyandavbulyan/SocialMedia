package com.blbulyandavbulyan.socialmedia.services;

import com.blbulyandavbulyan.socialmedia.entites.User;
import com.blbulyandavbulyan.socialmedia.utils.JWTTokenUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Spy
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Mock
    private UserService userService;
    @Mock
    private JWTTokenUtils jwtTokenUtils;
    @InjectMocks
    private AuthService authService;

    @Test
    @DisplayName("register user")
    public void registerUser() {
        String username = "david";
        String password = "123234323";
        String email = "test@gmail.com";
        assertDoesNotThrow(() -> authService.registerUser(username, password, email));
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userService, Mockito.times(1)).save(userCaptor.capture());
        User actualUser = userCaptor.getValue();
        assertTrue(passwordEncoder.matches(password, actualUser.getPassword()), "Passwords doesn't match!");
        assertEquals(username, actualUser.getUsername(), "User names are not equal!");
        assertEquals(email, actualUser.getEmail(), "Emails doesn't equal!");
    }

    @Test
    @DisplayName("authorize with valid credentials")
    public void authorizeWithValidCredentials() {
        String password = "1234556";
        String username = "david";
        String email = "test@gmail.com";
        String expectedToken = "blablabla";
        User user = new User(username, passwordEncoder.encode(password), email);
        Mockito.when(userService.loadUserByUsername(user.getUsername())).thenReturn(user);
        Mockito.when(jwtTokenUtils.generateToken(username, user.getAuthorities())).thenReturn(expectedToken);
        String actualToken = authService.authorize(username, password);
        assertSame(expectedToken, actualToken);
        Mockito.verify(jwtTokenUtils, Mockito.times(1)).generateToken(username, user.getAuthorities());
        Mockito.verify(userService, Mockito.times(1)).loadUserByUsername(user.getUsername());
    }
}