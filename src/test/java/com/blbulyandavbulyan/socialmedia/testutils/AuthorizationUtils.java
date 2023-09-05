package com.blbulyandavbulyan.socialmedia.testutils;

import com.blbulyandavbulyan.socialmedia.utils.JWTTokenUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@AllArgsConstructor
public class AuthorizationUtils {
    private JWTTokenUtils jwtTokenUtils;

    public HttpHeaders generateHeaders(String username, Collection<? extends GrantedAuthority> authorities) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Bearer " + jwtTokenUtils.generateToken(username, authorities));
        return httpHeaders;
    }
}
