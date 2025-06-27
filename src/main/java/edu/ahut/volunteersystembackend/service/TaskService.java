package edu.ahut.volunteersystembackend.service;

import edu.ahut.volunteersystembackend.dto.task.TaskDTO.*;

import java.util.List;

public interface TaskService {
    void createTask(CreateTask request);

    List<TaskInfo> getAllTasks();

    TaskInfo getTask(Long id);

    void joinTask(TaskRegistrationRequest request, String nickname, String email);

    void deleteTask(Long taskId);

    TaskInfo updateTask(CreateTask request);

    List<TaskDetail> getTaskDetails();

    ParticipantStatus getTaskStatus(Long taskId, String nickname);

    AuditResponse getTaskAuditDetail(Long taskId);

    void approveVolunteer(Long taskId, String email);

    void rejectVolunteer(Long taskId, String email);
}
