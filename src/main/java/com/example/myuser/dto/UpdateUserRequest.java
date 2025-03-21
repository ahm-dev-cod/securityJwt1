package com.example.myuser.dto;

import lombok.Data;

@Data
public class UpdateUserRequest {
    private String username;

    private String password;

    private String firstName;

    private String lastName;

    private String phoneNumber;
}