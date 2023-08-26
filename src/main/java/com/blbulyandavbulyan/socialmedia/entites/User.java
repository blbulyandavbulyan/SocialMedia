package com.blbulyandavbulyan.socialmedia.entites;

import com.blbulyandavbulyan.socialmedia.annotations.validation.ValidUserEmail;
import com.blbulyandavbulyan.socialmedia.annotations.validation.ValidUserName;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
@Entity
@Table(name = "users")
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    @ValidUserName
    @Column(nullable = false)
    private String username;
    @NotBlank
    @Column(nullable = false)
    @Size(min = 10, message = "Password too short")
    private String password;
    @ValidUserEmail
    @Column(nullable = false)
    private String email;

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}