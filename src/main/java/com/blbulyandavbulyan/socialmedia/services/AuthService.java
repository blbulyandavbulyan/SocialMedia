package com.blbulyandavbulyan.socialmedia.services;

import com.blbulyandavbulyan.socialmedia.entites.User;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {
    private PasswordEncoder passwordEncoder;
    private UserService userService;

    public void registerUser(String username, String password, String email){
        userService.save(new User(username, passwordEncoder.encode(password), email));
    }
    public String authorize(String username, String password){
        // TODO: 25.08.2023 реализовать метод авторизации, который будет возвращать JWT токен
        return null;
    }
}
