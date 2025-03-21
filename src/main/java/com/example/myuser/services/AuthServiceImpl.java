package com.example.myuser.services;

import com.example.myuser.entities.MyUser;
import com.example.myuser.repositories.MyUserRepository;
import com.example.myuser.util.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    private MyUserRepository myUserRepository;
    private BCryptPasswordEncoder passwordEncoder;
    @Override
    public String login(String username, String password) {
        MyUser user = myUserRepository.findByUsername(username);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new AuthenticationException("Identifiants invalides") {};
        }
        return JwtUtil.generateToken(username, user.getRole());
    }
}
