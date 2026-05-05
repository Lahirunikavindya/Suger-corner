package com.sugarcorner.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/**
 * Feedback entity - represents customer feedback and inquiries.
 */
@Entity
@Table(name = "feedbacks")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    @NotBlank
    @Size(min = 5, max = 200)
    @Column(nullable = false, length = 200)
    private String subject;

    @NotBlank
    @Size(min = 10, max = 2000)
    @Column(nullable = false, length = 2000)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MessageStatus status = MessageStatus.NEW;

    @Column(length = 2000)
    private String adminResponse;

    @Column(name = "submitted_at", nullable = false, updatable = false)
    private LocalDateTime submittedAt;

    @Column(name = "responded_at")
    private LocalDateTime respondedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum MessageStatus {
        NEW,
        PENDING,
        RESOLVED
    }

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        submittedAt = now;
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getCustomer() { return customer; }
    public void setCustomer(User customer) { this.customer = customer; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public MessageStatus getStatus() { return status; }
    public void setStatus(MessageStatus status) { this.status = status; }
    public String getAdminResponse() { return adminResponse; }
    public void setAdminResponse(String adminResponse) { this.adminResponse = adminResponse; }
    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }
    public LocalDateTime getRespondedAt() { return respondedAt; }
    public void setRespondedAt(LocalDateTime respondedAt) { this.respondedAt = respondedAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
