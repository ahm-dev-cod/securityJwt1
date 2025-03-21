package com.example.myuser.services;

import com.example.myuser.dto.RegisterRequest;
import com.example.myuser.dto.UpdateUserRequest;
import com.example.myuser.entities.MyUser;

import java.util.List;

public interface MyUserService {
    List<MyUser> getMyUsers();

    MyUser getMyUser(Long id);

    MyUser saveMyUser(MyUser myUser);

    MyUser updateUser(Long id, UpdateUserRequest updateRequest);
}