package edu.ahut.volunteersystembackend.dao;

import edu.ahut.volunteersystembackend.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskRepository  extends JpaRepository<Task,Long> {
    /**
     * 根据任务名称查找指定任务
     */
    Optional<Task> findByName(String name);

    /**
     * 判断任务是否存在
     */
    boolean existsByName(String name);

}
