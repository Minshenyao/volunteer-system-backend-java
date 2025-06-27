package edu.ahut.volunteersystembackend.dao;

import edu.ahut.volunteersystembackend.model.TaskParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TaskParticipantRepository extends JpaRepository<TaskParticipant, Long> {
    /**
     * 根据任务ID查找所有参与者
     */
    List<TaskParticipant> findByTaskId(Long taskId);

    /**
     * 根据任务ID和邮箱查找参与者
     */
    Optional<TaskParticipant> findByTaskIdAndEmail(Long taskId, String email);

    /**
     * 根据任务ID和昵称查找参与者
     */
    Optional<TaskParticipant> findByTaskIdAndNickname(Long taskId, String nickname);

    /**
     * 根据任务ID和状态查找参与者
     */
    @Query("SELECT tp FROM TaskParticipant tp WHERE tp.taskId = :taskId AND tp.status = :status")
    List<TaskParticipant> findByTaskIdAndStatus(@Param("taskId") Long taskId, @Param("status") Integer status);

    /**
     * 检查任务中是否存在指定邮箱的参与者
     */
    boolean existsByTaskIdAndEmail(Long taskId, String email);
}