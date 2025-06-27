package edu.ahut.volunteersystembackend.service.impl;

import edu.ahut.volunteersystembackend.dao.TaskParticipantRepository;
import edu.ahut.volunteersystembackend.dao.TaskRepository;
import edu.ahut.volunteersystembackend.dao.UserRepository;
import edu.ahut.volunteersystembackend.dto.task.TaskDTO;
import edu.ahut.volunteersystembackend.model.Task;
import edu.ahut.volunteersystembackend.model.TaskParticipant;
import edu.ahut.volunteersystembackend.model.User;
import edu.ahut.volunteersystembackend.service.TaskService;
import edu.ahut.volunteersystembackend.utils.TaskConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private static final Logger log = LoggerFactory.getLogger(TaskServiceImpl.class);
    private final TaskRepository taskRepository;
    private final TaskParticipantRepository taskParticipantRepository;
    private final UserRepository userRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, TaskParticipantRepository taskParticipantRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.taskParticipantRepository = taskParticipantRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void createTask(TaskDTO.CreateTask request) {
        // 验证任务名称是否已存在
        if (taskRepository.existsByName(request.getName())) {
            throw new RuntimeException("任务名称已存在");
        }

        // 验证时间是否合法
        if (request.getStartTime().compareTo(request.getEndTime()) >= 0) {
            throw new RuntimeException("开始时间必须早于结束时间");
        }

        if (request.getLimit() <= 0) {
            throw new RuntimeException("参与人数限制必须大于0");
        }

        Task task = new Task();
        task.setName(request.getName());
        task.setLocation(request.getLocation());
        task.setLimit(request.getLimit());
        task.setStartTime(request.getStartTime());
        task.setEndTime(request.getEndTime());
        task.setJoined(0);
        task.setCreateAt(new Date());
        taskRepository.save(task);
    }

    @Override
    public List<TaskDTO.TaskInfo> getAllTasks() {
        List<TaskDTO.TaskInfo> taskInfoList = new ArrayList<>();
        List<Task> taskList = taskRepository.findAll();
        for (Task task : taskList) {
            taskInfoList.add(TaskConverter.convertTaskToTaskInfo(task));
        }
        return taskInfoList;
    }

    @Override
    public TaskDTO.TaskInfo getTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("任务id:" + id + "不存在!"));
        return TaskConverter.convertTaskToTaskInfo(task);
    }

    @Override
    @Transactional
    public void joinTask(TaskDTO.TaskRegistrationRequest request, String nickname, String email) {
        log.info("email: {}, nickname: {}", email, nickname);
        // 验证任务是否存在
        Task task = taskRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("任务id:" + request.getId() + "不存在!"));

        // 验证人数是否已满
        if (task.getJoined() >= task.getLimit()) {
            throw new RuntimeException("任务参与人数已满");
        }

        // 验证是否已经参与过该任务
        if (taskParticipantRepository.existsByTaskIdAndEmail(task.getId(), email)) {
            throw new RuntimeException("您已经参与过该任务");
        }

        // 验证昵称是否已被使用
        Optional<TaskParticipant> participantByNickname = taskParticipantRepository.findByTaskIdAndNickname(task.getId(), nickname);
        if (participantByNickname.isPresent()) {
            throw new RuntimeException("此昵称已被使用");
        }

        // 创建参与者记录
        TaskParticipant participant = new TaskParticipant();
        participant.setTaskId(task.getId());
        participant.setNickname(nickname);
        participant.setEmail(email);
        participant.setStatus(0); // 0表示待审核
        participant.setCreatedAt(new Date());
        participant.setUpdatedAt(new Date());
        taskParticipantRepository.save(participant);

        // 更新任务的参与人数 (待审核也计入参与人数)
        task.setJoined(task.getJoined() + 1);
        taskRepository.save(task);
    }

    @Override
    @Transactional
    public void deleteTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("任务id:" + taskId + "不存在!"));

        // 删除任务的所有参与者
        List<TaskParticipant> participants = taskParticipantRepository.findByTaskId(taskId);
        taskParticipantRepository.deleteAll(participants);

        // 删除任务
        taskRepository.delete(task);
    }

    @Override
    @Transactional
    public TaskDTO.TaskInfo updateTask(TaskDTO.CreateTask request) {
        // 验证任务名称是否已存在
        Optional<Task> existingTaskByName = taskRepository.findByName(request.getName());
        if (existingTaskByName.isPresent() && !existingTaskByName.get().getName().equals(request.getName())) {
            throw new RuntimeException("任务名称已存在");
        }

        // 验证时间是否合法
        if (request.getStartTime().compareTo(request.getEndTime()) >= 0) {
            throw new RuntimeException("开始时间必须早于结束时间");
        }

        if (request.getLimit() <= 0) {
            throw new RuntimeException("参与人数限制必须大于0");
        }

        Task task = taskRepository.findByName(request.getName())
                .orElseThrow(() -> new RuntimeException("任务不存在"));

        // 如果新的人数限制小于已经参与的人数，不允许更新
        if (request.getLimit() < task.getJoined()) {
            throw new RuntimeException("新的人数限制不能小于已参与人数");
        }

        task.setName(request.getName());
        task.setLocation(request.getLocation());
        task.setLimit(request.getLimit());
        task.setStartTime(request.getStartTime());
        task.setEndTime(request.getEndTime());
        taskRepository.save(task);

        return TaskConverter.convertTaskToTaskInfo(task);
    }

    @Override
    public List<TaskDTO.TaskDetail> getTaskDetails() {
        List<Task> tasks = taskRepository.findAll();
        List<TaskDTO.TaskDetail> taskDetails = new ArrayList<>();

        for (Task task : tasks) {
            TaskDTO.TaskDetail detail = new TaskDTO.TaskDetail();
            detail.setId(task.getId());
            detail.setName(task.getName());
            detail.setCreatedAt(task.getCreateAt());
            detail.setStartTime(task.getStartTime());
            detail.setEndTime(task.getEndTime());
            detail.setLocation(task.getLocation());
            detail.setLimit(task.getLimit());
            detail.setJoined(task.getJoined());

            // 获取任务参与者
            List<TaskParticipant> participants = taskParticipantRepository.findByTaskId(task.getId());
            List<TaskDTO.ParticipantInfo> participantInfos = participants.stream()
                    .filter(participant -> participant.getStatus() == 1)
                    .map(participant -> {
                        TaskDTO.ParticipantInfo info = new TaskDTO.ParticipantInfo();
                        User user = userRepository.findByEmail(participant.getEmail());
                        info.setEmail(user.getEmail());
                        info.setNickname(user.getNickname());
                        if (user.getGender()) {
                            info.setGender("男");
                        } else {
                            info.setGender("女");
                        }
                        info.setAvatar(user.getAvatar());
                        info.setPhone(user.getPhone());
                        info.setDuration(user.getDuration());
                        info.setLastLoginTime(new Date(user.getLastLoginTime()));
                        return info;
                    })
                    .collect(Collectors.toList());

            detail.setParticipants(participantInfos);
            taskDetails.add(detail);
        }

        return taskDetails;
    }

    @Override
    public TaskDTO.ParticipantStatus getTaskStatus(Long taskId, String nickname) {
        // 验证任务是否存在
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("任务id:" + taskId + "不存在!"));

        // 查找参与者
        TaskParticipant participant = taskParticipantRepository.findByTaskIdAndNickname(taskId, nickname)
                .orElseThrow(() -> new RuntimeException("参与者不存在"));

        TaskDTO.ParticipantStatus status = new TaskDTO.ParticipantStatus();
        status.setId(participant.getId());
        status.setNickname(participant.getNickname());
        status.setStatus(participant.getStatus());

        return status;
    }

    @Override
    public TaskDTO.AuditResponse getTaskAuditDetail(Long taskId) {
        // 验证任务是否存在
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("任务id:" + taskId + "不存在!"));

        // 获取待审核的参与者
        List<TaskParticipant> pendingParticipants = taskParticipantRepository.findByTaskIdAndStatus(taskId, 0);

        List<TaskDTO.JoinTaskVolunteer> volunteers = pendingParticipants.stream()
                .map(participant -> {
                    TaskDTO.JoinTaskVolunteer volunteer = new TaskDTO.JoinTaskVolunteer();
                    volunteer.setNickname(participant.getNickname());
                    volunteer.setEmail(participant.getEmail());
                    volunteer.setStatus(participant.getStatus());
                    return volunteer;
                })
                .collect(Collectors.toList());

        TaskDTO.AuditResponse response = new TaskDTO.AuditResponse();
        response.setTaskId(taskId);
        response.setVolunteers(volunteers);

        return response;
    }

    @Override
    @Transactional
    public void approveVolunteer(Long taskId, String email) {
        // 验证任务是否存在
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("任务id:" + taskId + "不存在!"));

        // 查找参与者
        TaskParticipant participant = taskParticipantRepository.findByTaskIdAndEmail(taskId, email)
                .orElseThrow(() -> new RuntimeException("参与者不存在"));

        // 确认参与者状态为待审核
        if (participant.getStatus() != 0) {
            throw new RuntimeException("参与者状态不正确，当前状态为：" + participant.getStatus());
        }

        // 更新参与者状态为已通过 (1)
        participant.setStatus(1);
        participant.setUpdatedAt(new Date());
        taskParticipantRepository.save(participant);
    }

    @Override
    @Transactional
    public void rejectVolunteer(Long taskId, String email) {
        // 验证任务是否存在
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("任务id:" + taskId + "不存在!"));

        // 查找参与者
        TaskParticipant participant = taskParticipantRepository.findByTaskIdAndEmail(taskId, email)
                .orElseThrow(() -> new RuntimeException("参与者不存在"));

        // 确认参与者状态为待审核
        if (participant.getStatus() != 0) {
            throw new RuntimeException("参与者状态不正确，当前状态为：" + participant.getStatus());
        }

        // 更新参与者状态为已拒绝 (2)
        participant.setStatus(2);
        participant.setUpdatedAt(new Date());
        taskParticipantRepository.save(participant);

        // 更新任务的参与人数（减少一个）
        task.setJoined(task.getJoined() - 1);
        taskRepository.save(task);
    }
}