package com.taskmanagement.taskmanagement.domain.services;

import com.taskmanagement.taskmanagement.config.ApplicationConfiguration;
import com.taskmanagement.taskmanagement.domain.exception.AuthenticationException;
import com.taskmanagement.taskmanagement.domain.model.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    private ApplicationConfiguration applicationConfiguration;

    @Autowired
    private UserService userService;

    public User authentication(HttpServletRequest request) {
        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null) {
                throw new AuthenticationException("Adicione seu Authorization Bearer Token ao cabeçalho da requisição!");
            }
            String token = authHeader.substring(7);
            String email = applicationConfiguration.isTokenValid(token);
            User loggedUser = userService.getUserByEmail(email);
            if (loggedUser == null) {
                throw new AuthenticationException("Usuário não encontrado!");
            }
            return loggedUser;
        } catch (RuntimeException runtimeException) {
            throw new AuthenticationException(runtimeException.getMessage());
        }
    }
}
