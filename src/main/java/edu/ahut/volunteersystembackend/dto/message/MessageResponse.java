package edu.ahut.volunteersystembackend.dto.message;

import edu.ahut.volunteersystembackend.model.Message;

import java.time.format.DateTimeFormatter;

public class MessageResponse {
    private Long id;
    private String title;
    private String content;
    private String time;
    private String status;

    // Constructors
    public MessageResponse() {
    }

    public MessageResponse(Message message) {
        this.id = message.getId();
        this.title = message.getTitle();
        this.content = message.getContent();
        this.time = message.getTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.status = message.getStatus();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

