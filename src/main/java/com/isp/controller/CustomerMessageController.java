package com.isp.controller;

import com.isp.entity.CustomerMessage;
import com.isp.entity.CustomerMessage.MessageStatus;
import com.isp.service.CustomerMessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = "*")
public class CustomerMessageController {

    private final CustomerMessageService messageService;

    public CustomerMessageController(CustomerMessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/feedback")
    public ResponseEntity<CustomerMessage> submitFeedback(@RequestBody Map<String, String> request) {
        CustomerMessage message = messageService.submitFeedback(
                request.get("name"),
                request.get("email"),
                request.get("content")
        );
        return ResponseEntity.ok(message);
    }

    @PostMapping("/inquiry")
    public ResponseEntity<CustomerMessage> submitInquiry(@RequestBody Map<String, String> request) {
        CustomerMessage message = messageService.submitInquiry(
                request.get("name"),
                request.get("email"),
                request.get("content")
        );
        return ResponseEntity.ok(message);
    }

    @GetMapping
    public ResponseEntity<List<CustomerMessage>> getAllMessages() {
        return ResponseEntity.ok(messageService.getAllMessages());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerMessage> getMessage(@PathVariable Long id) {
        return messageService.getMessageById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/unresponded")
    public ResponseEntity<List<CustomerMessage>> getUnrespondedMessages() {
        return ResponseEntity.ok(messageService.getUnrespondedMessages());
    }

    @GetMapping("/responded")
    public ResponseEntity<List<CustomerMessage>> getRespondedMessages() {
        return ResponseEntity.ok(messageService.getRespondedMessages());
    }

    @GetMapping("/trends")
    public ResponseEntity<Map<String, Object>> getFeedbackTrends() {
        return ResponseEntity.ok(messageService.getFeedbackTrends());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<CustomerMessage> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> request) {
        MessageStatus status = MessageStatus.valueOf(request.get("status"));
        return ResponseEntity.ok(messageService.updateStatus(id, status));
    }

    @PostMapping("/{id}/respond")
    public ResponseEntity<CustomerMessage> respondToMessage(@PathVariable Long id, @RequestBody Map<String, String> request) {
        return ResponseEntity.ok(messageService.respondToMessage(id, request.get("response")));
    }
}
