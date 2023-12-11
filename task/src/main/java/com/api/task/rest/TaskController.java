package com.api.task.rest;

import com.api.task.entity.Progress;
import com.api.task.entity.Task;
import com.api.task.entity.User;
import com.api.task.entity.custom.CustomTaskCount;
import com.api.task.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping("/tasks")
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        Task createdTask = taskService.createTask(task);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    @PutMapping("/tasks/{taskId}")
    public ResponseEntity<Task> updateTask(@RequestBody Task task, @PathVariable("taskId") String taskId) {
        Task updatedTask = taskService.updateTask(task, Long.parseLong(taskId));
        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }

    @DeleteMapping("/tasks/{taskId}")
    public ResponseEntity<String> deleteTask(@PathVariable("taskId") String taskId) {
        taskService.deleteTask(Long.parseLong(taskId));
        return new ResponseEntity<>(taskId, HttpStatus.OK);
    }

    @GetMapping("/tasks")
    public ResponseEntity<List<Task>> getTasks() {
        return new ResponseEntity<>(taskService.getTasks(), HttpStatus.OK);
    }

    @PostMapping("/tasks/{taskId}/assign")
    public void assignTasks(@RequestBody User user, @PathVariable("taskId") String taskId) {
        taskService.assignTask(user, Long.parseLong(taskId));
    }

    @GetMapping("/users/{userId}/tasks")
    public ResponseEntity<List<Task>> getUserAssignedTasks(@PathVariable("userId") String userId) {
        return new ResponseEntity<>(taskService.getUserAssignedTasks(Long.parseLong(userId)), HttpStatus.OK);
    }

    @PutMapping("/tasks/{taskId}/progress")
    public void updateTaskProgress(@RequestBody Progress progress, @PathVariable("taskId") String taskId) {
        taskService.updateTaskProgress(progress, Long.parseLong(taskId));
    }

    @GetMapping("/tasks/overdue")
    public ResponseEntity<List<Task>> getOverDueTasks() {
        return new ResponseEntity<>(taskService.getOverDueTasks(), HttpStatus.OK);
    }

    @GetMapping("/tasks/status/{status}")
    public ResponseEntity<List<Task>> getTasksByStatus(@PathVariable("status") String status) {
        return new ResponseEntity<>(taskService.getTasksByStatus(status), HttpStatus.OK);
    }

    @GetMapping("/tasks/completed")
    public ResponseEntity<List<Task>> getCompletedTasks(@RequestParam("startDate") LocalDate startDate,
                                        @RequestParam("endDate") LocalDate endDate) {
        return new ResponseEntity<>(taskService.getCompletedTasks(startDate, endDate), HttpStatus.OK);
    }

    @GetMapping("/tasks/statistics")
    public ResponseEntity<List<CustomTaskCount>> getStatistics() {
        return new ResponseEntity<>(taskService.getStatistics(), HttpStatus.OK);
    }

    @GetMapping("/next")
    public Task getNextTask(@RequestBody User user) {
        return taskService.getNextTask(user);
    }

}
