package com.blbulyandavbulyan.socialmedia.entites;

import com.blbulyandavbulyan.socialmedia.annotations.validation.user.ValidUserEmail;
import com.blbulyandavbulyan.socialmedia.annotations.validation.user.ValidUserName;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "users")
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    @ValidUserName
    @Column(nullable = false, length = 50)
    private String username;
    @NotBlank
    @Column(nullable = false)
    @Size(min = 10, message = "Password too short")
    private String password;
    @ValidUserEmail
    @Column(nullable = false)
    private String email;
    @ManyToMany
    @JoinTable(
            name = "subscriptions",
            joinColumns = @JoinColumn(name = "subscriber_username"),
            inverseJoinColumns = @JoinColumn(name = "target_username")
    )
    private Set<User> subscriptions;
    @ManyToMany(mappedBy = "subscriptions")
    private Set<User> subscribers;
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