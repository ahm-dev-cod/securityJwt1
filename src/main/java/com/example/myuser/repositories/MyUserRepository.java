package com.example.myuser.repositories;

import com.example.myuser.entities.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MyUserRepository extends JpaRepository<MyUser, Long> {
    MyUser findByUsername(String username);
}
