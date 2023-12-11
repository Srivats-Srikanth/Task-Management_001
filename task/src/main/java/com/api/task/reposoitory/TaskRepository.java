package com.api.task.reposoitory;

import com.api.task.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    Task findByTaskId(Long taskId);

    List<Task> findAllByUserId(Long userId);

    @Query(value = "SELECT * FROM tasks t" +
            " WHERE t.due_date < NOW()::DATE  ",
    nativeQuery = true)
    List<Task> getOverDueTasks();

    List<Task> findAllByStatus(String status);

    @Query(value = "SELECT * FROM task t " +
            "WHERE t.completed_date >= :startDate AND t.completed_date <= :endDate",
    nativeQuery = true)
    List<Task> getCompletedTasks(LocalDate startDate, LocalDate endDate);


    @Query(nativeQuery = true,
            value = "WITH task_count AS (select count(task_id) AS totalCount, " +
                    "SUM(case when status = 'completed' then 1 else 0 end) AS completedCount " +
                    "from task " +
                    ") select totalCount AS totalCount, completedCount AS completedCount, ((cast(completedCount AS FLOAT) / CAST(totalCount AS FLOAT)) * 100 ) AS percentageCount " +
                    "from task_count")
    List<Map<String, Object>> getStatistics();
}
