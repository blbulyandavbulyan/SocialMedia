package com.blbulyandavbulyan.socialmedia.services;

import com.blbulyandavbulyan.socialmedia.entites.User;
import com.blbulyandavbulyan.socialmedia.exceptions.InvalidLoginOrPassword;
import com.blbulyandavbulyan.socialmedia.utils.JWTTokenUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {
    private PasswordEncoder passwordEncoder;
    private UserService userService;
    private JWTTokenUtils jwtTokenUtils;
    public void registerUser(String username, String password, String email){
        userService.save(new User(username, passwordEncoder.encode(password), email));
    }
    public String authorize(String username, String password){
        try{
            UserDetails userDetails = userService.loadUserByUsername(username);
            if(passwordEncoder.matches(password, userDetails.getPassword())){
                return jwtTokenUtils.generateToken(userDetails.getUsername(), userDetails.getAuthorities());
            }
            else throw new InvalidLoginOrPassword();
        }
        catch (UsernameNotFoundException e){
            throw new InvalidLoginOrPassword();
        }
    }
}
