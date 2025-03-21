package com.example.myuser.dto;

import com.example.myuser.entities.Role;
import lombok.Data;

@Data
public class RegisterRequest {
    private String username;

    private String password;

    private Role role;

    private String firstName;

    private String lastName;

    private String phoneNumber;
}
