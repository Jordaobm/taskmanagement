package com.taskmanagement.taskmanagement.domain.services;

import com.taskmanagement.taskmanagement.api.dtos.input.TaskInputDTO;
import com.taskmanagement.taskmanagement.domain.enums.StatusEnum;
import com.taskmanagement.taskmanagement.domain.exception.TaskInvalid;
import com.taskmanagement.taskmanagement.domain.model.Task;
import com.taskmanagement.taskmanagement.domain.model.User;
import com.taskmanagement.taskmanagement.domain.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserService userService;

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public List<Task> findAllTasksByUser(Long userId) {
        return taskRepository.findAllByAuthorIdOrderByCreatedAtAsc(userId);
    }

    public Task findById(Long taskId) {
        return taskRepository.findById(taskId).get();
    }

    public void validateTask(TaskInputDTO task, Boolean isCreated) {
        if (task.getTitle().length() < 5) {
            throw new TaskInvalid("O título deve ter pelo menos 5 caracteres.");
        }

        if (isCreated) {
            List<Task> tasks = taskRepository.findAllByAuthorIdAndStatusOrderByCreatedAtAsc(task.getAuthorId(), StatusEnum.PENDING.getId());

            if (tasks.size() >= 10) {
                throw new TaskInvalid("Um usuário não pode criar mais de 10 tarefas pendentes ao mesmo tempo.");
            }
        }
    }

    public Task createTask(TaskInputDTO task) {
        validateTask(task, true);

        User author = userService.getUserById(task.getAuthorId());

        Task newTask = new Task();

        newTask.setTitle(task.getTitle());
        newTask.setDescription(task.getDescription());
        newTask.setStatus(StatusEnum.valueOf(StatusEnum.PENDING.getId()));
        newTask.setAuthor(author);

        taskRepository.save(newTask);

        return newTask;
    }


    public Task updateTask(TaskInputDTO task) {
        validateTask(task, false);

        Task updatedTask = findById(task.getId());

        updatedTask.setTitle(task.getTitle());
        updatedTask.setDescription(task.getDescription());
        updatedTask.setStatus(StatusEnum.valueOf(task.getStatus()));

        taskRepository.save(updatedTask);

        return updatedTask;
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}