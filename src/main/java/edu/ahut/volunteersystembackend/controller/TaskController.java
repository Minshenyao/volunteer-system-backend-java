package edu.ahut.volunteersystembackend.controller;

import edu.ahut.volunteersystembackend.dto.task.TaskDTO;
import edu.ahut.volunteersystembackend.response.Response;
import edu.ahut.volunteersystembackend.service.TaskService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/task")
public class TaskController {
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/tasks")
    public Response<Map<String, List<TaskDTO.TaskInfo>>> getAllTasks() {
        try {
            List<TaskDTO.TaskInfo> taskInfo = taskService.getAllTasks();
            Map<String, List<TaskDTO.TaskInfo>> data = new HashMap<>();
            data.put("tasks", taskInfo);
            return Response.success("查询成功", data);
        } catch (Exception e) {
            return Response.error(500, "查询失败: " + e.getMessage());
        }
    }
    @GetMapping("/getTaskStatus")
    public Response<Map<String, TaskDTO.ParticipantStatus>> getTaskStatus(HttpServletRequest request, @RequestParam("TaskId") Long taskId) {
        String nickname = (String) request.getAttribute("nickname");
        try {
            TaskDTO.ParticipantStatus status = taskService.getTaskStatus(taskId, nickname);
            Map<String, TaskDTO.ParticipantStatus> data = new HashMap<>();
            data.put("taskStatus", status);
            return Response.success("查询成功", data);
        } catch (Exception e) {
            return Response.error(404, e.getMessage());
        }
    }

    @PostMapping("/join")
    public Response<Void> joinTask(HttpServletRequest request, @RequestBody TaskDTO.TaskRegistrationRequest taskRegistrationRequest) {
        String nickname = (String) request.getAttribute("nickname");
        String email = (String) request.getAttribute("email");
        try {
            taskService.joinTask(taskRegistrationRequest, nickname, email);
            return Response.success("加入任务成功", null);
        } catch (Exception e) {
            return Response.error(400, e.getMessage());
        }
    }

    @GetMapping("/getTaskDetails")
    public Response<Map<String, List<TaskDTO.TaskDetail>>> getTaskDetails() {
        try {
            List<TaskDTO.TaskDetail> taskDetails = taskService.getTaskDetails();
            Map<String, List<TaskDTO.TaskDetail>> data = new HashMap<>();
            data.put("taskDetails", taskDetails);
            return Response.success("查询成功", data);
        } catch (Exception e) {
            return Response.error(500, "查询失败: " + e.getMessage());
        }
    }
}