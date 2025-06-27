package edu.ahut.volunteersystembackend.utils;

import edu.ahut.volunteersystembackend.dto.task.TaskDTO;
import edu.ahut.volunteersystembackend.model.Task;
import edu.ahut.volunteersystembackend.model.TaskParticipant;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

public class TaskConverter {
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    static {
        DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
    }

    /**
     * 将Task实体转换为TaskInfo DTO
     */
    public static TaskDTO.TaskInfo convertTaskToTaskInfo(Task task) {
        TaskDTO.TaskInfo taskInfo = new TaskDTO.TaskInfo();
        taskInfo.setId(task.getId());
        taskInfo.setName(task.getName());
        taskInfo.setJoined(task.getJoined());
        taskInfo.setLocation(task.getLocation());
        taskInfo.setLimit(task.getLimit());
        taskInfo.setCreatedAt(task.getCreateAt());
        taskInfo.setStartTime(task.getStartTime());
        taskInfo.setEndTime(task.getEndTime());
        return taskInfo;
    }

    /**
     * 将Task实体和参与者列表转换为TaskDetail DTO
     */
    public static TaskDTO.TaskDetail convertToTaskDetail(Task task, List<TaskParticipant> participants) {
        TaskDTO.TaskDetail detail = new TaskDTO.TaskDetail();
        detail.setId(task.getId());
        detail.setName(task.getName());
        detail.setCreatedAt(task.getCreateAt());
        detail.setStartTime(task.getStartTime());
        detail.setEndTime(task.getEndTime());
        detail.setLocation(task.getLocation());
        detail.setLimit(task.getLimit());
        detail.setJoined(task.getJoined());

        List<TaskDTO.ParticipantInfo> participantInfos = participants.stream()
                .map(TaskConverter::convertToParticipantInfo)
                .collect(Collectors.toList());

        detail.setParticipants(participantInfos);
        return detail;
    }

    /**
     * 将TaskParticipant实体转换为ParticipantInfo DTO
     */
    public static TaskDTO.ParticipantInfo convertToParticipantInfo(TaskParticipant participant) {
        TaskDTO.ParticipantInfo info = new TaskDTO.ParticipantInfo();
        info.setEmail(participant.getEmail());
        info.setNickname(participant.getNickname());
        // 其他字段如性别、头像等可能需要从用户表中获取
        // 这里只设置基本信息
        return info;
    }

    /**
     * 将TaskParticipant实体转换为ParticipantStatus DTO
     */
    public static TaskDTO.ParticipantStatus convertToParticipantStatus(TaskParticipant participant) {
        TaskDTO.ParticipantStatus status = new TaskDTO.ParticipantStatus();
        status.setId(participant.getId());
        status.setNickname(participant.getNickname());
        status.setStatus(participant.getStatus());
        return status;
    }

    /**
     * 将TaskParticipant实体转换为JoinTaskVolunteer DTO
     */
    public static TaskDTO.JoinTaskVolunteer convertToJoinTaskVolunteer(TaskParticipant participant) {
        TaskDTO.JoinTaskVolunteer volunteer = new TaskDTO.JoinTaskVolunteer();
        volunteer.setNickname(participant.getNickname());
        volunteer.setEmail(participant.getEmail());
        volunteer.setStatus(participant.getStatus());
        return volunteer;
    }

    /**
     * 格式化时间戳为可读字符串
     */
    public static String formatTimestamp(Long timestamp) {
        if (timestamp == null) {
            return null;
        }
        return DATE_FORMAT.format(new Date(timestamp));
    }
}