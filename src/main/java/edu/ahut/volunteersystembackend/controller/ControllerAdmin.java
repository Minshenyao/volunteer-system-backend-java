package edu.ahut.volunteersystembackend.controller;

import edu.ahut.volunteersystembackend.dto.task.TaskDTO;
import edu.ahut.volunteersystembackend.response.Response;
import edu.ahut.volunteersystembackend.service.TaskService;
import edu.ahut.volunteersystembackend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class ControllerAdmin {
    private static final Logger log = LoggerFactory.getLogger(ControllerAdmin.class);
    private final TaskService taskService;
    private final UserService userService;

    @Autowired
    public ControllerAdmin(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
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

    @PostMapping("/create_task")
    public Response<Void> createTask(@RequestBody TaskDTO.CreateTask createTask) {
        try {
            taskService.createTask(createTask);
            return Response.success("创建任务成功", null);
        } catch (Exception e) {
            return Response.error(500, "创建任务失败: " + e.getMessage());
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

    @DeleteMapping("/delete_task")
    public Response<Void> deleteTask(HttpServletRequest request, @RequestParam("TaskId") Long taskId) {
//        Long taskId = Long.parseLong(request.getParameter("id"));
        try {
            taskService.deleteTask(taskId);
            return Response.success("删除任务成功", null);
        } catch (Exception e) {
            return Response.error(500, "删除任务失败: " + e.getMessage());
        }
    }

    @PutMapping("/update")
    public Response<TaskDTO.TaskInfo> updateTask(@RequestBody TaskDTO.CreateTask createTask) {
        try {
            TaskDTO.TaskInfo taskInfo = taskService.updateTask(createTask);
            return Response.success("更新任务成功", taskInfo);
        } catch (Exception e) {
            return Response.error(500, "更新任务失败: " + e.getMessage());
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

    @GetMapping("/getTaskStatus")
    public Response<Map<String, TaskDTO.ParticipantStatus>> getTaskStatus(HttpServletRequest request, @RequestParam("TaskId") Long taskId) {
//        Long taskId = Long.parseLong(request.getParameter("taskId"));
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

    @PostMapping("/GetTaskAuditDetail")
    public Response<Map<String, TaskDTO.AuditResponse>> getTaskAuditDetail(HttpServletRequest request, @RequestBody Map<String, Object> requestMap) {
        try {
            Long taskId = ((Number) requestMap.get("taskId")).longValue();  // 确保是 Long 类型
            TaskDTO.AuditResponse auditResponse = taskService.getTaskAuditDetail(taskId);
            Map<String, TaskDTO.AuditResponse> data = new HashMap<>();
            data.put("taskDetails", auditResponse);
            return Response.success("查询成功", data);
        } catch (Exception e) {
            return Response.error(404, e.getMessage());
        }
    }

    @PostMapping("/approveVolunteer")
    public Response<Void> approveVolunteer(@RequestBody TaskDTO.HandleVolunteerRequest handleVolunteerRequest) {
        try {
            taskService.approveVolunteer(handleVolunteerRequest.getTaskId(), handleVolunteerRequest.getEmail());
            return Response.success("审核通过成功", null);
        } catch (Exception e) {
            return Response.error(400, e.getMessage());
        }
    }

    @PostMapping("/rejectVolunteer")
    public Response<Void> rejectVolunteer(@RequestBody TaskDTO.HandleVolunteerRequest handleVolunteerRequest) {
        try {
            taskService.rejectVolunteer(handleVolunteerRequest.getTaskId(), handleVolunteerRequest.getEmail());
            return Response.success("拒绝成功", null);
        } catch (Exception e) {
            return Response.error(400, e.getMessage());
        }
    }
    @GetMapping("/volunteer_count")
    public Response<Map<String, Object>> GetAllUserInfo(HttpServletRequest request) {
        try {
            Map<String, Object> volunteersData = userService.GetAllUserInfo();
            return Response.success("获取志愿者信息成功", volunteersData);
        } catch (Exception e) {
            return Response.error(500, "获取志愿者信息失败: " + e.getMessage());
        }
    }
}