package edu.ahut.volunteersystembackend.service;

import edu.ahut.volunteersystembackend.model.Message;

import java.util.List;

public interface MessageService {
    List<Message> getMessagesByUserId(Long userId);
    Message createMessage(Long userId, String title, String content);
    boolean markMessageAsRead(Long messageId);
}
