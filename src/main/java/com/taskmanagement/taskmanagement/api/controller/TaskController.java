package com.taskmanagement.taskmanagement.api.controller;

import com.taskmanagement.taskmanagement.api.dtos.input.TaskInputDTO;
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
        User author = authenticationService.authentication(request);

        Task newTask = taskService.createTask(author, task);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newTask.getId()).toUri();
        return ResponseEntity.created(uri).body(newTask);
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<Task>> findAll(HttpServletRequest request) {
        User author = authenticationService.authentication(request);

        List<Task> tasks = taskService.findAll();
        return ResponseEntity.ok().body(tasks);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<Task>> findAllTasksByUser(@PathVariable Long id, HttpServletRequest request) {
        User author = authenticationService.authentication(request);

        List<Task> tasks = taskService.findAllTasksByUser(id);
        return ResponseEntity.ok().body(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> findById(@PathVariable Long id, HttpServletRequest request) {
        User author = authenticationService.authentication(request);

        Task task = taskService.findById(id);
        return ResponseEntity.ok().body(task);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody TaskInputDTO task, HttpServletRequest request) {
        User author = authenticationService.authentication(request);

        task.setId(id);
        Task updatedTask = taskService.updateTask(author, task);
        return ResponseEntity.ok().body(updatedTask);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id, HttpServletRequest request) {
        User author = authenticationService.authentication(request);

        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

}
