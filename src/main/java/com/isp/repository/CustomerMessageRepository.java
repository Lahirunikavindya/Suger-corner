package com.isp.repository;

import com.isp.entity.CustomerMessage;
import com.isp.entity.CustomerMessage.MessageStatus;
import com.isp.entity.CustomerMessage.MessageType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CustomerMessageRepository extends JpaRepository<CustomerMessage, Long> {

    List<CustomerMessage> findAllByOrderByCreatedAtDesc();

    List<CustomerMessage> findByStatus(MessageStatus status);

    List<CustomerMessage> findByType(MessageType type);

    List<CustomerMessage> findByStatusOrderByCreatedAtDesc(MessageStatus status);

    @Query("SELECT m.type, COUNT(m) FROM CustomerMessage m GROUP BY m.type")
    List<Object[]> countByType();

    @Query("SELECT m.status, COUNT(m) FROM CustomerMessage m GROUP BY m.status")
    List<Object[]> countByStatus();

    long countByStatus(MessageStatus status);

    long countByType(MessageType type);
}
