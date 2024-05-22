package com.taskmanagement.taskmanagement.config;

import com.taskmanagement.taskmanagement.api.dtos.input.UserInputDTO;
import com.taskmanagement.taskmanagement.domain.enums.PermissionEnum;
import com.taskmanagement.taskmanagement.domain.model.User;
import com.taskmanagement.taskmanagement.domain.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Override
    public void run(String... args) throws Exception {
        User user = userService.getUserByEmail("usuariocomum@example.com");

        if (user == null) {
            User logged = new User();
            logged.setId(1L);
            logged.setName("Usuario Administrador");
            logged.setEmail("usuarioadministrador@example.com");
            logged.setPassword("administrador");
            logged.setRole(PermissionEnum.ADMINISTRADOR);

            UserInputDTO admin = new UserInputDTO();
            admin.setId(1L);
            admin.setName("Usuario Administrador");
            admin.setEmail("usuarioadministrador@example.com");
            admin.setPassword("administrador");

            userService.createUserAdmin(logged, admin);
        }
    }
}
