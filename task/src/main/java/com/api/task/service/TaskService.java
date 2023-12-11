package com.api.task.service;

import com.api.task.entity.Progress;
import com.api.task.entity.Task;
import com.api.task.entity.User;
import com.api.task.entity.custom.CustomTaskCount;
import com.api.task.error.NoTaskFoundException;
import com.api.task.reposoitory.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

@Service
public class TaskService {

    private final String COMPLETED = "completed";

    @Autowired
    private TaskRepository taskRepository;

    private PriorityQueue<Task> taskQueue = new PriorityQueue<>();

    public Task createTask(Task task) {
        taskQueue.add(task);
        return taskRepository.save(task);
    }

    public Task updateTask(Task task, Long taskId) {
        return Optional.ofNullable(findByTaskId(taskId))
                .map(existingTask -> {
                    task.setTaskId(existingTask.getTaskId());
                    if (existingTask.getCompletedDate() == null && task.getStatus() != null
                            && task.getStatus().equalsIgnoreCase(COMPLETED)) {
                        task.setCompletedDate(LocalDate.now());
                    }
                    return taskRepository.save(task); // Return the updated object
                })
                .orElse(task);
    }

    public List<Task> getTasks() {
        return taskRepository.findAll();
    }

    private Task findByTaskId(Long taskId) {
        return Optional.ofNullable(taskRepository.findByTaskId(taskId)).
                orElseThrow(() -> new NoTaskFoundException("Task not found with id: " + taskId));
    }

    public void deleteTask(Long taskId) {
        Task existingTask = findByTaskId(taskId);
        if (null != existingTask) {
            taskRepository.delete(existingTask);
        }
    }

    public void assignTask(User user, Long taskId) {
        Optional.ofNullable(findByTaskId(taskId)).ifPresent(existingTask -> {
            existingTask.setUserId(user.getUserId());
            taskRepository.save(existingTask);
        });
    }

    public List<Task> getUserAssignedTasks(Long userId) {
        return taskRepository.findAllByUserId(userId);
    }

    public void updateTaskProgress(Progress progress, Long taskId) {
        Optional.ofNullable(findByTaskId(taskId)).ifPresent(existingTask -> {
            if (null != progress && null != progress.getProgressPercentage()) {
                existingTask.setProgressPercentage(progress.getProgressPercentage());
                taskRepository.save(existingTask);
            }
        });
    }

    public List<Task> getOverDueTasks() {
        return taskRepository.getOverDueTasks();
    }

    public List<Task> getTasksByStatus(String status) {
        return taskRepository.findAllByStatus(status);
    }

    public List<Task> getCompletedTasks(LocalDate startDate, LocalDate endDate) {
        return taskRepository.getCompletedTasks(startDate, endDate);
    }

    public List<CustomTaskCount> getStatistics() {
        return Optional.ofNullable(taskRepository.getStatistics()).
                map(row -> row.stream().map(this::getCustomTaskCount)).orElse(Stream.of(new CustomTaskCount())).toList();
    }

    private CustomTaskCount getCustomTaskCount(Map<String, Object> statistics) {
        return new CustomTaskCount((Long) statistics.get("totalCount"), (Long) statistics.get("completedCount"), (Double) statistics.get("percentageCount"));
    }

    public Task getNextTask(User user) {
        return Optional.ofNullable(taskQueue.poll()).map(task -> {
            assignTask(user, task.getTaskId());
            return task;
        }).orElse(null);
    }
}
