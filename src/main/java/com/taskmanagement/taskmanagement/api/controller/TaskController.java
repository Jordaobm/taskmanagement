package com.taskmanagement.taskmanagement.api.controller;

import com.mysql.cj.util.StringUtils;
import com.taskmanagement.taskmanagement.api.dtos.input.TaskInputDTO;
import com.taskmanagement.taskmanagement.domain.exception.TaskException;
import com.taskmanagement.taskmanagement.domain.exception.UserException;
import com.taskmanagement.taskmanagement.domain.model.Task;
import com.taskmanagement.taskmanagement.domain.model.User;
import com.taskmanagement.taskmanagement.domain.services.AuthenticationService;
import com.taskmanagement.taskmanagement.domain.services.TaskService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody TaskInputDTO task, HttpServletRequest request) {
        if (StringUtils.isNullOrEmpty(task.getTitle()) || StringUtils.isNullOrEmpty(task.getDescription())) {
            throw new TaskException("Preencha os campos title e description para cadastrar uma nova tarefa!");
        }
        User loggedUser = authenticationService.authentication(request);
        Task newTask = taskService.createTask(loggedUser, task);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newTask.getId()).toUri();
        return ResponseEntity.created(uri).body(newTask);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Task>> findAll(HttpServletRequest request) {
        User loggedUser = authenticationService.authentication(request);
        List<Task> tasks = taskService.findAll(loggedUser);
        return ResponseEntity.ok().body(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> findById(@PathVariable Long id, HttpServletRequest request) {
        if (id == null) {
            throw new TaskException("Informe o ID da tarefa!");
        }

        User loggedUser = authenticationService.authentication(request);
        Task task = taskService.findById(loggedUser, id);
        return ResponseEntity.ok().body(task);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody TaskInputDTO task, HttpServletRequest request) {
        if (id == null) {
            throw new TaskException("Informe o ID da tarefa!");
        }
        if (StringUtils.isNullOrEmpty(task.getTitle()) && StringUtils.isNullOrEmpty(task.getDescription()) && task.getStatus() == null) {
            throw new TaskException("Preencha os campos title e description para editar uma tarefa!");
        }

        User loggedUser = authenticationService.authentication(request);
        task.setId(id);
        Task updatedTask = taskService.updateTask(loggedUser, task);
        return ResponseEntity.ok().body(updatedTask);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id, HttpServletRequest request) {
        if (id == null) {
            throw new TaskException("Informe o ID da tarefa!");
        }

        User loggedUser = authenticationService.authentication(request);
        taskService.deleteTask(loggedUser, id);
        return ResponseEntity.noContent().build();
    }

}
