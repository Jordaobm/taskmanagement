package com.taskmanagement.taskmanagement.api.controller;

import com.mysql.cj.util.StringUtils;
import com.taskmanagement.taskmanagement.api.dtos.input.UserInputDTO;
import com.taskmanagement.taskmanagement.api.dtos.output.UserLoginOutputDTO;
import com.taskmanagement.taskmanagement.domain.exception.UserException;
import com.taskmanagement.taskmanagement.domain.model.User;
import com.taskmanagement.taskmanagement.domain.services.AuthenticationService;
import com.taskmanagement.taskmanagement.domain.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserInputDTO user) {
        if (StringUtils.isNullOrEmpty(user.getEmail()) || StringUtils.isNullOrEmpty(user.getName()) || StringUtils.isNullOrEmpty(user.getPassword())) {
            throw new UserException("Preencha os campos email, name e password para cadastrar um usuário!");
        }

        User newUser = userService.createUser(user);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newUser.getId()).toUri();
        return ResponseEntity.created(uri).body(newUser);
    }

    @PostMapping("/admin")
    public ResponseEntity<User> createUserAdmin(@RequestBody UserInputDTO user, HttpServletRequest request) {
        if (StringUtils.isNullOrEmpty(user.getEmail()) || StringUtils.isNullOrEmpty(user.getName()) || StringUtils.isNullOrEmpty(user.getPassword())) {
            throw new UserException("Preencha os campos email, name e password para cadastrar um usuário!");
        }

        User loggedUser = authenticationService.authentication(request);
        User newUser = userService.createUserAdmin(loggedUser, user);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newUser.getId()).toUri();
        return ResponseEntity.created(uri).body(newUser);
    }


    @PostMapping("/login")
    public ResponseEntity<UserLoginOutputDTO> loginUser(@RequestBody UserInputDTO user) {
        if (StringUtils.isNullOrEmpty(user.getEmail()) || StringUtils.isNullOrEmpty(user.getPassword())) {
            throw new UserException("Preencha os campos email e password para realizar login!");
        }

        UserLoginOutputDTO token = userService.loginUser(user);
        return ResponseEntity.ok().body(token);
    }
}
