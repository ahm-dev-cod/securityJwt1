package com.example.myuser.security;

import com.example.myuser.entities.Role;
import com.example.myuser.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        logger.debug("Authorization Header: {}", header);
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            if (JwtUtil.validateToken(token)) {
                String username = JwtUtil.getUsernameFromToken(token);
                Role role = JwtUtil.getRoleFromToken(token);
                logger.debug("Utilisateur authentifié : {} avec rôle : {}", username, role);
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.name());
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        username, null, Collections.singletonList(authority));
                SecurityContextHolder.getContext().setAuthentication(auth);
            } else {
                logger.warn("Token invalide : {}", token);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token invalide");
                return;
            }
        }
        // Passe au filtre suivant ou au contrôleur
        filterChain.doFilter(request, response);
    }
}
