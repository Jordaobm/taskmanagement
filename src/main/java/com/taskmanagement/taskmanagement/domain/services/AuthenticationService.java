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
            String token = authHeader.substring(7);
            String email = applicationConfiguration.isTokenValid(token);
            User user = userService.getUserByEmail(email);
            if (user == null) {
                throw new AuthenticationException("Usuário não encontrado!");
            }
            return user;
        } catch (RuntimeException runtimeException) {
            throw new AuthenticationException(runtimeException.getMessage());
        }
    }
}
