package com.sugarcorner.repository;

import com.sugarcorner.model.entity.Order;
import com.sugarcorner.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByCustomerOrderByCreatedAtDesc(User customer);

    List<Order> findByOrderByCreatedAtDesc();

    List<Order> findByStatus(Order.OrderStatus status);
}
