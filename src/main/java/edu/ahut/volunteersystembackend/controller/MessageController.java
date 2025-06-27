package edu.ahut.volunteersystembackend.controller;

import edu.ahut.volunteersystembackend.dto.message.CreateMessageRequest;
import edu.ahut.volunteersystembackend.dto.message.MessageResponse;
import edu.ahut.volunteersystembackend.model.Message;
import edu.ahut.volunteersystembackend.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/message")
public class MessageController {

    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getMessagesByUserId(@PathVariable Long userId) {
        try {
            List<Message> messages = messageService.getMessagesByUserId(userId);
            List<MessageResponse> messageResponses = messages.stream()
                    .map(MessageResponse::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(messageResponses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("无法获取消息", e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> createMessage(@RequestBody CreateMessageRequest request) {
        try {
            Message message = messageService.createMessage(
                    request.getUserId(),
                    request.getTitle(),
                    request.getContent()
            );
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new MessageResponse(message));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("创建消息失败", e.getMessage()));
        }
    }

    @PutMapping("/{messageId}/read")
    public ResponseEntity<?> markMessageAsRead(@PathVariable Long messageId) {
        try {
            boolean success = messageService.markMessageAsRead(messageId);
            if (success) {
                return ResponseEntity.ok(new SuccessResponse("标记为已读的消息"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("消息不存在", "未找到ID为" + messageId + "的消息"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("无法将消息标记为已读", e.getMessage()));
        }
    }

    // Inner classes for error and success responses
    private static class ErrorResponse {
        private final String error;
        private final String message;

        public ErrorResponse(String error, String message) {
            this.error = error;
            this.message = message;
        }

        public String getError() {
            return error;
        }

        public String getMessage() {
            return message;
        }
    }

    private static class SuccessResponse {
        private final String message;

        public SuccessResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
