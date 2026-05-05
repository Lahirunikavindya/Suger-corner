package com.sugarcorner.service;

import com.sugarcorner.dto.FeedbackRequest;
import com.sugarcorner.model.entity.Feedback;
import com.sugarcorner.model.entity.User;
import com.sugarcorner.repository.FeedbackRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;

    public FeedbackService(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    @Transactional
    public Feedback submitFeedback(User customer, FeedbackRequest request) {
        Feedback feedback = new Feedback();
        feedback.setCustomer(customer);
        feedback.setSubject(request.subject());
        feedback.setMessage(request.message());
        feedback.setStatus(Feedback.MessageStatus.NEW);
        return feedbackRepository.save(feedback);
    }

    public List<Feedback> getByCustomer(Long customerId) {
        return feedbackRepository.findByCustomerIdOrderBySubmittedAtDesc(customerId);
    }

    public List<Feedback> getAllFeedbacks() {
        return feedbackRepository.findByOrderBySubmittedAtDesc();
    }

    public Feedback getById(Long id) {
        return feedbackRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Feedback not found"));
    }

    @Transactional
    public Feedback respond(Long feedbackId, String adminResponse) {
        Feedback feedback = getById(feedbackId);
        feedback.setAdminResponse(adminResponse);
        feedback.setStatus(Feedback.MessageStatus.RESOLVED);
        feedback.setRespondedAt(LocalDateTime.now());
        return feedbackRepository.save(feedback);
    }

    @Transactional
    public Feedback updateStatus(Long feedbackId, Feedback.MessageStatus status) {
        Feedback feedback = getById(feedbackId);
        feedback.setStatus(status);
        return feedbackRepository.save(feedback);
    }
}
