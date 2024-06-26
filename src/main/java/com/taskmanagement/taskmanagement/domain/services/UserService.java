package com.taskmanagement.taskmanagement.domain.services;

import com.taskmanagement.taskmanagement.api.dtos.input.UserInputDTO;
import com.taskmanagement.taskmanagement.api.dtos.output.UserLoginOutputDTO;
import com.taskmanagement.taskmanagement.config.ApplicationConfiguration;
import com.taskmanagement.taskmanagement.domain.enums.PermissionEnum;
import com.taskmanagement.taskmanagement.domain.exception.AuthenticationException;
import com.taskmanagement.taskmanagement.domain.exception.UserException;
import com.taskmanagement.taskmanagement.domain.model.User;
import com.taskmanagement.taskmanagement.domain.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApplicationConfiguration applicationConfiguration;

    public User getUserByEmail(String email) {
        return userRepository.findOneByEmail(email);
    }

    public String encodePassword(String password) {
        PasswordEncoder passwordEncoder = applicationConfiguration.passwordEncoder();
        return passwordEncoder.encode(password);
    }

    public Boolean matchPassword(String receivedPassword, String databasePassword) {
        PasswordEncoder passwordEncoder = applicationConfiguration.passwordEncoder();
        return passwordEncoder.matches(receivedPassword, databasePassword);
    }

    public User createUser(UserInputDTO user) {
        if (userRepository.findOneByEmail(user.getEmail()) != null) {
            throw new UserException("Usuário já existente com este e-mail!");
        }

        User newUser = new User();

        newUser.setName(user.getName());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(encodePassword(user.getPassword()));
        newUser.setRole(PermissionEnum.USER);
        userRepository.save(newUser);
        return newUser;
    }

    public User createUserAdmin(User loggedUser, UserInputDTO user) {
        if (loggedUser.getRole() != PermissionEnum.ADMINISTRADOR) {
            throw new AuthenticationException("Somente usuários administradores podem adicionar usuários administradores");
        }

        if (userRepository.findOneByEmail(user.getEmail()) != null) {
            throw new UserException("Usuário já existente com este e-mail!");
        }

        User newUser = new User();

        newUser.setName(user.getName());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(encodePassword(user.getPassword()));
        newUser.setRole(PermissionEnum.ADMINISTRADOR);
        userRepository.save(newUser);
        return newUser;
    }

    public UserLoginOutputDTO loginUser(UserInputDTO user) {
        User databaseUser = getUserByEmail(user.getEmail());

        if (!matchPassword(user.getPassword(), databaseUser.getPassword())) {
            throw new AuthenticationException("Credenciais inválidas!");
        }

        UserLoginOutputDTO userLoginOutputDTO = new UserLoginOutputDTO();
        userLoginOutputDTO.setAccessToken(applicationConfiguration.generateToken(databaseUser));

        return userLoginOutputDTO;
    }

}
