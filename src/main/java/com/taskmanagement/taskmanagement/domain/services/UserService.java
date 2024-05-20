package com.taskmanagement.taskmanagement.domain.services;

import com.taskmanagement.taskmanagement.domain.exception.UserAlreadyExist;
import com.taskmanagement.taskmanagement.domain.model.User;
import com.taskmanagement.taskmanagement.domain.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    public User createUser(User user) {

        if (userRepository.findOneByEmail(user.getEmail()) != null) {
            throw new UserAlreadyExist("Usuário já existente com este e-mail!");
        }

        User newUser = new User();

        newUser.setName(user.getName());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(user.getPassword());
        userRepository.save(newUser);
        return newUser;
    }


}
