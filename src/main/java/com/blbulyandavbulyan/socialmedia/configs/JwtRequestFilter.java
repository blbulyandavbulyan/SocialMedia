package com.blbulyandavbulyan.socialmedia.configs;

import com.blbulyandavbulyan.socialmedia.utils.JWTTokenUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {
    private final JWTTokenUtils jwtTokenUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if(authHeader != null && authHeader.startsWith("Bearer ")){
            try{
                String jwt = authHeader.substring(7);
                String username = jwtTokenUtils.getUserName(jwt);
                if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
                    SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                            username,
                            null,
                            jwtTokenUtils.getRoles(jwt).stream().map(SimpleGrantedAuthority::new).toList()
                    ));
                }
            }
            catch (ExpiredJwtException e){
                // TODO: 25.08.2023 добавить здесь выброс более конкретного исключения, чтобы потом оно запаковалось controller advice в json ошибку
                log.debug("token has expired");
            }
            catch (SignatureException e){
                // TODO: 25.08.2023 добавить здесь выброс более конкретного исключения, чтобы потом оно запаковалось controller advice в json ошибку
                log.debug("incorrect signature for token");
            }
        }
        filterChain.doFilter(request, response);
    }
}