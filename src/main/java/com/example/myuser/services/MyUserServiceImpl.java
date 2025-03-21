package com.example.myuser.services;

import com.example.myuser.dto.UpdateUserRequest;
import com.example.myuser.entities.MyUser;
import com.example.myuser.entities.Role;
import com.example.myuser.repositories.MyUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MyUserServiceImpl implements MyUserService {
    private MyUserRepository myUserRepository;
    private BCryptPasswordEncoder passwordEncoder;
    @Override
    public List<MyUser> getMyUsers() {
        return myUserRepository.findAll();
    }

    @Override
    public MyUser getMyUser(Long id) {
        return myUserRepository.findById(id).get();
    }

    @Override
    public MyUser saveMyUser(MyUser myUser) {
        if (myUserRepository.findByUsername(myUser.getUsername()) != null) {
            throw new IllegalArgumentException("Cet utilisateur existe déjà");
        }
        MyUser myUser1 = new MyUser();
        myUser1.setUsername(myUser.getUsername());
        myUser1.setPassword(passwordEncoder.encode(myUser.getPassword()));
        myUser1.setRole(myUser.getRole() != null ? myUser.getRole() : Role.ROLE_STUDENT);
        myUser1.setFirstName(myUser.getFirstName());
        myUser1.setLastName(myUser.getLastName());
        myUser1.setPhoneNumber(myUser.getPhoneNumber());

        return myUserRepository.save(myUser1);
    }

    @Override
    public MyUser updateUser(Long id, UpdateUserRequest updateRequest) {
        MyUser user = myUserRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));

        // Vérifier si le nouveau username est déjà pris par un autre utilisateur
        MyUser existingUser = myUserRepository.findByUsername(updateRequest.getUsername());
        if (existingUser != null && !existingUser.getId().equals(id)) {
            throw new IllegalArgumentException("Ce nom d'utilisateur est déjà pris");
        }

        // Mettre à jour les champs modifiables (sauf role)
        user.setUsername(updateRequest.getUsername());
        user.setPassword(passwordEncoder.encode(updateRequest.getPassword()));
        user.setFirstName(updateRequest.getFirstName());
        user.setLastName(updateRequest.getLastName());
        user.setPhoneNumber(updateRequest.getPhoneNumber());

        return myUserRepository.save(user);
    }
    }
