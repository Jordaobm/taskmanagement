package com.taskmanagement.taskmanagement.domain.services;

import com.mysql.cj.util.StringUtils;
import com.taskmanagement.taskmanagement.api.dtos.input.TaskInputDTO;
import com.taskmanagement.taskmanagement.domain.enums.PermissionEnum;
import com.taskmanagement.taskmanagement.domain.enums.StatusEnum;
import com.taskmanagement.taskmanagement.domain.exception.TaskException;
import com.taskmanagement.taskmanagement.domain.model.Task;
import com.taskmanagement.taskmanagement.domain.model.User;
import com.taskmanagement.taskmanagement.domain.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserService userService;

    public List<Task> findAll(User loggedUser) {
        if (loggedUser.getRole() == PermissionEnum.ADMINISTRADOR) {
            return taskRepository.findAll();
        }
        return taskRepository.findAllByAuthorIdOrderByCreatedAtAsc(loggedUser.getId());
    }

    public Task findById(User loggedUser, Long taskId) {
        try {
            Optional<Task> task = taskRepository.findById(taskId);

            if (task.isEmpty()) {
                throw new TaskException("Tarefa não encontrada!");
            }

            if (loggedUser.getRole() != PermissionEnum.ADMINISTRADOR && loggedUser.getId() != task.get().getAuthor().getId()) {
                throw new TaskException("Usuários com a role USER só podem visualizar/editar/deletar tarefas que são de sua autoria!");
            }

            return task.get();

        } catch (RuntimeException runtimeException) {
            throw new TaskException(runtimeException.getMessage());

        }
    }

    public void validateTask(User author, TaskInputDTO task, Boolean isCreated) {
        if (!StringUtils.isNullOrEmpty(task.getTitle())) {
            if (task.getTitle().length() < 5) {
                throw new TaskException("O título deve ter pelo menos 5 caracteres.");
            }
        }

        if (isCreated) {
            List<Task> tasks = taskRepository.findAllByAuthorIdAndStatusOrderByCreatedAtAsc(author.getId(), StatusEnum.PENDING.getId());

            if (tasks.size() >= 10) {
                throw new TaskException("Um usuário não pode criar mais de 10 tarefas pendentes ao mesmo tempo.");
            }
        }
    }

    public Task createTask(User loggedUser, TaskInputDTO task) {
        validateTask(loggedUser, task, true);

        Task newTask = new Task();

        newTask.setTitle(task.getTitle());
        newTask.setDescription(task.getDescription());
        newTask.setStatus(StatusEnum.valueOf(StatusEnum.PENDING.getId()));
        newTask.setAuthor(loggedUser);

        taskRepository.save(newTask);

        return newTask;
    }


    public Task updateTask(User loggedUser, TaskInputDTO task) {
        validateTask(loggedUser, task, false);

        Task updatedTask = findById(loggedUser, task.getId());

        updatedTask.setTitle(StringUtils.isNullOrEmpty(task.getTitle()) ? updatedTask.getTitle() : task.getTitle());
        updatedTask.setDescription(StringUtils.isNullOrEmpty(task.getDescription()) ? updatedTask.getDescription() : task.getDescription());
        updatedTask.setStatus(task.getStatus() == null ? updatedTask.getStatus() : StatusEnum.valueOf(task.getStatus()));

        taskRepository.save(updatedTask);

        return updatedTask;
    }

    public void deleteTask(User loggedUser, Long id) {
        Task task = findById(loggedUser, id);
        taskRepository.deleteById(task.getId());
    }
}
