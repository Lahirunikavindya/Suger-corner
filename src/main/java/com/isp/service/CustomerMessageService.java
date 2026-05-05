package com.isp.service;

import com.isp.entity.CustomerMessage;
import com.isp.entity.CustomerMessage.MessageStatus;
import com.isp.entity.CustomerMessage.MessageType;
import com.isp.repository.CustomerMessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CustomerMessageService {

    private final CustomerMessageRepository repository;

    public CustomerMessageService(CustomerMessageRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public CustomerMessage submitFeedback(String name, String email, String content) {
        CustomerMessage message = new CustomerMessage(name, email, MessageType.FEEDBACK, content);
        return repository.save(message);
    }

    @Transactional
    public CustomerMessage submitInquiry(String name, String email, String content) {
        CustomerMessage message = new CustomerMessage(name, email, MessageType.INQUIRY, content);
        return repository.save(message);
    }

    public List<CustomerMessage> getAllMessages() {
        return repository.findAllByOrderByCreatedAtDesc();
    }

    public Optional<CustomerMessage> getMessageById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public CustomerMessage updateStatus(Long id, MessageStatus status) {
        CustomerMessage message = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found: " + id));
        message.setStatus(status);
        return repository.save(message);
    }

    @Transactional
    public CustomerMessage respondToMessage(Long id, String adminResponse) {
        CustomerMessage message = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found: " + id));
        message.setAdminResponse(adminResponse);
        message.setStatus(MessageStatus.RESOLVED);
        message.setRespondedAt(LocalDateTime.now());
        return repository.save(message);
    }

    public List<CustomerMessage> getUnrespondedMessages() {
        return repository.findByStatusOrderByCreatedAtDesc(MessageStatus.NEW);
    }

    public List<CustomerMessage> getRespondedMessages() {
        return repository.findByStatusOrderByCreatedAtDesc(MessageStatus.RESOLVED);
    }

    public Map<String, Object> getFeedbackTrends() {
        Map<String, Object> trends = new HashMap<>();
        trends.put("totalMessages", repository.count());
        trends.put("newCount", repository.countByStatus(MessageStatus.NEW));
        trends.put("pendingCount", repository.countByStatus(MessageStatus.PENDING));
        trends.put("resolvedCount", repository.countByStatus(MessageStatus.RESOLVED));
        trends.put("feedbackCount", repository.countByType(MessageType.FEEDBACK));
        trends.put("inquiryCount", repository.countByType(MessageType.INQUIRY));
        return trends;
    }
}
