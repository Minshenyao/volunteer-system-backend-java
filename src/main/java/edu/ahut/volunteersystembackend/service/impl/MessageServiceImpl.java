package edu.ahut.volunteersystembackend.service.impl;

import edu.ahut.volunteersystembackend.dao.MessageRepository;
import edu.ahut.volunteersystembackend.model.Message;
import edu.ahut.volunteersystembackend.service.MessageService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;

    @Autowired
    public MessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public List<Message> getMessagesByUserId(Long userId) {
        return messageRepository.findByUserId(userId);
    }

    @Override
    @Transactional
    public Message createMessage(Long userId, String title, String content) {
        Message message = new Message();
        message.setUserId(userId);
        message.setTitle(title);
        message.setContent(content);
        message.setTime(LocalDateTime.now());
        message.setStatus("unread");

        return messageRepository.save(message);
    }

    @Override
    @Transactional
    public boolean markMessageAsRead(Long messageId) {
        Optional<Message> messageOpt = messageRepository.findById(messageId);
        if (messageOpt.isPresent()) {
            Message message = messageOpt.get();
            message.setStatus("read");
            messageRepository.save(message);
            return true;
        }
        return false;
    }
}