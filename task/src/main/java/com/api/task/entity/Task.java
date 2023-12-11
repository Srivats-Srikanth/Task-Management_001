package com.api.task.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long taskId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "priority", nullable = false)
    private String priority;

    @Column(name = "status")
    private String status;

    @Column(name = "completed_date")
    private LocalDate completedDate;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "progress_percentage")
    private Integer progressPercentage;

}
