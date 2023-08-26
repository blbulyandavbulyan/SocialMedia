package com.blbulyandavbulyan.socialmedia.services;


import com.blbulyandavbulyan.socialmedia.entites.User;
import com.blbulyandavbulyan.socialmedia.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findById(username).orElseThrow(()->new UsernameNotFoundException("User with name " + username + " not found!"));
    }

    public boolean existByUsername(String username) {
        // TODO: 25.08.2023 сделать реализацию 
        return false;
    }

    public void save(User user) {
        // TODO: 25.08.2023 сделать реализацию
    }
}
