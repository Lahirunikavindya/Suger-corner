package com.sugarcorner.repository;

import com.sugarcorner.model.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    List<Feedback> findByCustomerIdOrderBySubmittedAtDesc(Long customerId);

    List<Feedback> findByOrderBySubmittedAtDesc();

    List<Feedback> findByStatus(Feedback.MessageStatus status);
}
