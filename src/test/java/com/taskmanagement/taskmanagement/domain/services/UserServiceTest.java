package com.taskmanagement.taskmanagement.domain.services;

import com.taskmanagement.taskmanagement.api.dtos.input.UserInputDTO;
import com.taskmanagement.taskmanagement.api.dtos.output.UserLoginOutputDTO;
import com.taskmanagement.taskmanagement.config.ApplicationConfiguration;
import com.taskmanagement.taskmanagement.domain.enums.PermissionEnum;
import com.taskmanagement.taskmanagement.domain.exception.AuthenticationException;
import com.taskmanagement.taskmanagement.domain.exception.UserException;
import com.taskmanagement.taskmanagement.domain.model.User;
import com.taskmanagement.taskmanagement.domain.repositories.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ApplicationConfiguration applicationConfiguration;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("it should be able to find user by email")
    void getUserByEmail() {

        String email = "usuarioadministrador@example.com";

        User user = new User();
        user.setId(1L);
        user.setName("Usuario Administrador");
        user.setEmail("usuarioadministrador@example.com");
        user.setPassword("administrador");
        user.setRole(PermissionEnum.ADMINISTRADOR);

        when(userRepository.findOneByEmail(email)).thenReturn(user);

        User findUser = userService.getUserByEmail(email);

        assertEquals("Usuario Administrador", findUser.getName());
        assertEquals("usuarioadministrador@example.com", findUser.getEmail());
        assertEquals("administrador", findUser.getPassword());
        assertEquals(PermissionEnum.ADMINISTRADOR.getId(), findUser.getRole().getId());

        verify(userRepository, times(1)).findOneByEmail(email);
    }

    @Test
    @DisplayName("it should be able to encode password")
    void encodePassword() {

        String password = "administrador";

        when(applicationConfiguration.passwordEncoder()).thenReturn(passwordEncoder);
        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");

        String encodedPassword = userService.encodePassword(password);

        assertEquals("encodedPassword", encodedPassword);
        verify(applicationConfiguration, times(1)).passwordEncoder();
        verify(passwordEncoder, times(1)).encode(password);
    }

    @Test
    @DisplayName("it should be able to match password")
    void matchPassword() {

        String password = "administrador";
        String databasePassword = "administrador";

        when(applicationConfiguration.passwordEncoder()).thenReturn(passwordEncoder);
        when(passwordEncoder.matches(password, databasePassword)).thenReturn(true);

        Boolean match = userService.matchPassword(password, databasePassword);

        assertEquals(true, match);
        verify(applicationConfiguration, times(1)).passwordEncoder();
        verify(passwordEncoder, times(1)).matches(password, databasePassword);
    }

    @Test
    @DisplayName("it should be able to create User")
    void createUser() {
        // Testando exceção de e-mail já cadastrado
        when(applicationConfiguration.passwordEncoder()).thenReturn(passwordEncoder);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        User user = new User();
        user.setName("Usuario Comum");
        user.setEmail("usuariocomum@example.com");
        user.setPassword("usuariocomum");

        UserInputDTO userInputDTO = new UserInputDTO();
        userInputDTO.setName("Usuario Comum");
        userInputDTO.setEmail("usuariocomum@example.com");
        userInputDTO.setPassword("usuariocomum");

        when(userRepository.findOneByEmail(userInputDTO.getEmail())).thenReturn(user);

        UserException exception = assertThrows(UserException.class, () -> {
            userService.createUser(userInputDTO);
        });

        assertEquals("Usuário já existente com este e-mail!", exception.getMessage());

        // Testando criação do usuário
        when(userRepository.findOneByEmail(userInputDTO.getEmail())).thenReturn(null);
        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.createUser(userInputDTO);

        assertEquals("Usuario Comum", createdUser.getName());
        assertEquals("usuariocomum@example.com", createdUser.getEmail());
        assertEquals("encodedPassword", createdUser.getPassword());
        assertEquals(PermissionEnum.USER.getId(), createdUser.getRole().getId());

        verify(userRepository, times(1)).save(any(User.class));
        verify(passwordEncoder, times(1)).encode("usuariocomum");
    }

    @Test
    @DisplayName("it should be able to create User Admin")
    void createUserAdmin() {
        // Testando exceção de e-mail já cadastrado
        when(applicationConfiguration.passwordEncoder()).thenReturn(passwordEncoder);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        User logged = new User();
        logged.setName("Usuario Administrador");
        logged.setEmail("usuarioadministrador@example.com");
        logged.setPassword("usuarioadministrador");
        logged.setRole(PermissionEnum.ADMINISTRADOR);


        User user = new User();
        user.setName("Usuario Administrador");
        user.setEmail("usuarioadministrador@example.com");
        user.setPassword("usuarioadministrador");

        UserInputDTO userInputDTO = new UserInputDTO();
        userInputDTO.setName("Usuario Administrador");
        userInputDTO.setEmail("usuarioadministrador@example.com");
        userInputDTO.setPassword("usuarioadministrador");

        when(userRepository.findOneByEmail(userInputDTO.getEmail())).thenReturn(user);

        UserException userAlreadyExists = assertThrows(UserException.class, () -> {
            userService.createUserAdmin(logged, userInputDTO);
        });

        assertEquals("Usuário já existente com este e-mail!", userAlreadyExists.getMessage());

        // Testando cadastrar usuário administrador não tendo permissão
        when(userRepository.findOneByEmail(userInputDTO.getEmail())).thenReturn(null);

        logged.setRole(PermissionEnum.USER);

        AuthenticationException userLoggedNotHavePermission = assertThrows(AuthenticationException.class, () -> {
            userService.createUserAdmin(logged, userInputDTO);
        });

        assertEquals("Somente usuários administradores podem adicionar usuários administradores", userLoggedNotHavePermission.getMessage());

        // Testando criação do usuário
        logged.setRole(PermissionEnum.ADMINISTRADOR);
        when(userRepository.findOneByEmail(userInputDTO.getEmail())).thenReturn(null);
        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.createUserAdmin(logged, userInputDTO);

        assertEquals("Usuario Administrador", createdUser.getName());
        assertEquals("usuarioadministrador@example.com", createdUser.getEmail());
        assertEquals("encodedPassword", createdUser.getPassword());
        assertEquals(PermissionEnum.ADMINISTRADOR.getId(), createdUser.getRole().getId());

        verify(userRepository, times(1)).save(any(User.class));
        verify(passwordEncoder, times(1)).encode("usuarioadministrador");
    }

    @Test
    @DisplayName("it should be able to login User")
    void loginUser() {

        String databasePassword = "usuarioadministrador";

        User user = new User();
        user.setName("Usuario Administrador");
        user.setEmail("usuarioadministrador@example.com");
        user.setPassword(databasePassword);
        user.setRole(PermissionEnum.ADMINISTRADOR);

        UserInputDTO login = new UserInputDTO();
        login.setEmail("usuarioadministrador@example.com");
        login.setPassword(databasePassword);

        when(applicationConfiguration.passwordEncoder()).thenReturn(passwordEncoder);
        when(applicationConfiguration.generateToken(user)).thenReturn("accessToken");
        when(userService.getUserByEmail(user.getEmail())).thenReturn(user);
        when(passwordEncoder.matches(login.getPassword(), databasePassword)).thenReturn(true);

        UserLoginOutputDTO accessToken = userService.loginUser(login);

        assertEquals(accessToken.getAccessToken(), "accessToken");
        verify(applicationConfiguration, times(1)).generateToken(user);

    }
}
