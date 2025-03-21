package com.example.myuser.controllers;

import com.example.myuser.dto.RegisterRequest;
import com.example.myuser.dto.UpdateUserRequest;
import com.example.myuser.entities.MyUser;
import com.example.myuser.services.MyUserService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(path = "users")
public class MyUserController {
    private MyUserService myUserService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    List<MyUser> getAllUsers() {
        return myUserService.getMyUsers();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<MyUser> getUserById(@PathVariable Long id) {
        MyUser user = myUserService.getMyUser(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping("/register")
    public ResponseEntity<MyUser> registerUser(@RequestBody MyUser myUser) {
        MyUser savedUser = myUserService.saveMyUser(myUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }
    private static final Logger logger = LoggerFactory.getLogger(MyUserController.class);
    @PutMapping("/me")
    public ResponseEntity<MyUser> updateUser(@RequestBody UpdateUserRequest updateRequest) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.debug("Utilisateur connecté: {}", currentUsername);
        logger.debug("Autorités: {}", SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        MyUser currentUser = myUserService.getMyUsers().stream()
                .filter(user -> user.getUsername().equals(currentUsername))
                .findFirst()
                .orElse(null);
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        MyUser updatedUser = myUserService.updateUser(currentUser.getId(), updateRequest);
        return ResponseEntity.ok(updatedUser);
    }
}
