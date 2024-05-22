package com.taskmanagement.taskmanagement.domain.services;

import com.taskmanagement.taskmanagement.api.dtos.input.TaskInputDTO;
import com.taskmanagement.taskmanagement.domain.enums.PermissionEnum;
import com.taskmanagement.taskmanagement.domain.enums.StatusEnum;
import com.taskmanagement.taskmanagement.domain.exception.TaskException;
import com.taskmanagement.taskmanagement.domain.exception.UserException;
import com.taskmanagement.taskmanagement.domain.model.Task;
import com.taskmanagement.taskmanagement.domain.model.User;
import com.taskmanagement.taskmanagement.domain.repositories.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @InjectMocks
    private TaskService taskService;

    @Mock
    private TaskRepository taskRepository;

    @Test
    @DisplayName("it should be able to findAll tasks")
    void findAll() {
        // Buscando tarefas tendo permissão de ADMINISTRADOR
        User user = new User();
        user.setId(1L);
        user.setName("User");
        user.setEmail("user@example.com");
        user.setPassword("user");
        user.setRole(PermissionEnum.ADMINISTRADOR);


        Task task = new Task();
        task.setId(1L);
        task.setTitle("Task Title");
        task.setDescription("Task Description");
        task.setAuthor(user);

        Task task2 = new Task();
        task2.setId(1L);
        task2.setTitle("Task Title 2");
        task2.setDescription("Task Description 2");
        task2.setAuthor(user);

        List<Task> tasks = new ArrayList<Task>();
        tasks.add(task);
        tasks.add(task2);

        List<Task> tasks2 = new ArrayList<Task>();
        tasks2.add(task2);

        when(taskRepository.findAll()).thenReturn(tasks);
        when(taskRepository.findAllByAuthorIdOrderByCreatedAtAsc(user.getId())).thenReturn(tasks2);

        List<Task> findAll = taskService.findAll(user);

        // Buscando tarefas tendo permissão de USER

        user.setRole(PermissionEnum.USER);
        List<Task> findAllByAuthorIdOrderByCreatedAtAsc = taskService.findAll(user);

        assertEquals(findAll.get(0).getTitle(), "Task Title");
        assertEquals(findAll.get(1).getTitle(), "Task Title 2");
        assertEquals(findAllByAuthorIdOrderByCreatedAtAsc.get(0).getTitle(), "Task Title 2");

        verify(taskRepository, times(1)).findAll();
        verify(taskRepository, times(1)).findAllByAuthorIdOrderByCreatedAtAsc(user.getId());

    }

    @Test
    @DisplayName("it should be able to findById task")
    void findById() {

        User admin = new User();
        admin.setId(1L);
        admin.setName("admin");
        admin.setEmail("admin@example.com");
        admin.setPassword("admin");
        admin.setRole(PermissionEnum.ADMINISTRADOR);

        User user = new User();
        user.setId(2L);
        user.setName("User");
        user.setEmail("user@example.com");
        user.setPassword("user");
        user.setRole(PermissionEnum.USER);

        Task task = new Task();
        task.setId(1L);
        task.setTitle("Task Title");
        task.setDescription("Task Description");
        task.setAuthor(admin);

        when(taskRepository.findById(task.getId())).thenReturn(null);

        TaskException exception = assertThrows(TaskException.class, () -> {
            taskService.findById(admin, task.getId());
        });

        assertEquals("Tarefa não encontrada!", exception.getMessage());

        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));

        TaskException exception2 = assertThrows(TaskException.class, () -> {
            taskService.findById(user, task.getId());
        });

        assertEquals("Usuários com a role USER só podem visualizar/editar/deletar tarefas que são de sua autoria!", exception2.getMessage());

    }

    @Test
    @DisplayName("it should be able to validate Task")
    void validateTask() {

        User admin = new User();
        admin.setId(1L);
        admin.setName("admin");
        admin.setEmail("admin@example.com");
        admin.setPassword("admin");
        admin.setRole(PermissionEnum.ADMINISTRADOR);

        TaskInputDTO taskInputDTO = new TaskInputDTO();
        taskInputDTO.setId(1L);
        taskInputDTO.setTitle("Task");
        taskInputDTO.setDescription("Task Description");

        TaskException exception = assertThrows(TaskException.class, () -> {
            taskService.validateTask(admin, taskInputDTO, true);
        });

        assertEquals("O título deve ter pelo menos 5 caracteres.", exception.getMessage());

        List<Task> taskList = new ArrayList<>();
        taskInputDTO.setTitle("Titulo da tarefa");

        for (int i = 0; i < 10; i++) {
            Task t = new Task();
            t.setId((long) i);
            t.setTitle("Titulo da tarefa " + i);
            t.setDescription("Descricao da tarefa " + i);
            taskList.add(t);
        }

        when(taskRepository.findAllByAuthorIdAndStatusOrderByCreatedAtAsc(admin.getId(), StatusEnum.PENDING.getId())).thenReturn(taskList);

        TaskException exception2 = assertThrows(TaskException.class, () -> {
            taskService.validateTask(admin, taskInputDTO, true);
        });

        assertEquals("Um usuário não pode criar mais de 10 tarefas pendentes ao mesmo tempo.", exception2.getMessage());


    }

    @Test
    @DisplayName("it should be able to create Task")
    void createTask() {
        User admin = new User();
        admin.setId(1L);
        admin.setName("admin");
        admin.setEmail("admin@example.com");
        admin.setPassword("admin");
        admin.setRole(PermissionEnum.ADMINISTRADOR);

        TaskInputDTO taskInputDTO = new TaskInputDTO();
        taskInputDTO.setId(1L);
        taskInputDTO.setTitle("Titulo da tarefa");
        taskInputDTO.setDescription("Descricao da tarefa");

        Task createdTask = taskService.createTask(admin, taskInputDTO);

        assertEquals(createdTask.getTitle(), taskInputDTO.getTitle());
        assertEquals(createdTask.getDescription(), taskInputDTO.getDescription());
        assertEquals(createdTask.getAuthor().getName(), admin.getName());

    }


    @Test
    @DisplayName("it should be able to update Task")
    void updateTask() {
        User admin = new User();
        admin.setId(1L);
        admin.setName("admin");
        admin.setEmail("admin@example.com");
        admin.setPassword("admin");
        admin.setRole(PermissionEnum.ADMINISTRADOR);


        Task task = new Task();
        task.setId(1L);
        task.setTitle("Titulo da tarefa");
        task.setDescription("Descricao da tarefa");
        task.setAuthor(admin);
        task.setStatus(StatusEnum.PENDING);

        TaskInputDTO taskInputDTO = new TaskInputDTO();
        taskInputDTO.setId(1L);
        taskInputDTO.setTitle("Titulo da tarefa");
        taskInputDTO.setDescription("Descricao da tarefa");
        taskInputDTO.setStatus(StatusEnum.PENDING.getId());

        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));

        Task updateTask = taskService.updateTask(admin, taskInputDTO);

        assertEquals(updateTask.getTitle(), taskInputDTO.getTitle());
        assertEquals(updateTask.getDescription(), taskInputDTO.getDescription());
        assertEquals(updateTask.getAuthor().getName(), admin.getName());

    }

    @Test
    @DisplayName("it should be able to delete Task")
    void deleteTask() {
        User admin = new User();
        admin.setId(1L);
        admin.setName("admin");
        admin.setEmail("admin@example.com");
        admin.setPassword("admin");
        admin.setRole(PermissionEnum.ADMINISTRADOR);

        Task task = new Task();
        task.setId(1L);
        task.setTitle("Titulo da tarefa");
        task.setDescription("Descricao da tarefa");
        task.setAuthor(admin);
        task.setStatus(StatusEnum.PENDING);

        TaskInputDTO taskInputDTO = new TaskInputDTO();
        taskInputDTO.setId(1L);
        taskInputDTO.setTitle("Titulo da tarefa");
        taskInputDTO.setDescription("Descricao da tarefa");
        taskInputDTO.setStatus(StatusEnum.PENDING.getId());

        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));

        taskService.deleteTask(admin, taskInputDTO.getId());

        verify(taskRepository, times(1)).deleteById(task.getId());

    }

}
