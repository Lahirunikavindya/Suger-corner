package com.sugarcorner.service;

import com.sugarcorner.dto.OrderItemRequest;
import com.sugarcorner.dto.OrderRequest;
import com.sugarcorner.model.entity.*;
import com.sugarcorner.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final PaymentService paymentService;

    public OrderService(OrderRepository orderRepository, ProductService productService, PaymentService paymentService) {
        this.orderRepository = orderRepository;
        this.productService = productService;
        this.paymentService = paymentService;
    }

    @Transactional
    public Order placeOrder(User customer, OrderRequest request) {
        Order order = new Order();
        order.setCustomer(customer);
        order.setDeliveryAddress(request.deliveryAddress());
        order.setNotes(request.notes());

        List<OrderItem> items = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemRequest itemReq : request.items()) {
            Product product = productService.getById(itemReq.productId());
            productService.reduceStock(product.getId(), itemReq.quantity());

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(itemReq.quantity());
            item.setUnitPrice(product.getPrice());
            item.setSubtotal(product.getPrice().multiply(BigDecimal.valueOf(itemReq.quantity())));
            items.add(item);
            total = total.add(item.getSubtotal());
        }

        order.setOrderItems(items);
        order.setTotalAmount(total);
        order = orderRepository.save(order);

        Payment payment = paymentService.createPayment(order);
        order.setPayment(payment);
        return orderRepository.save(order);
    }

    public List<Order> getOrdersByCustomer(User customer) {
        return orderRepository.findByCustomerOrderByCreatedAtDesc(customer);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findByOrderByCreatedAtDesc();
    }

    public Order getById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
    }

    @Transactional
    public Order updateStatus(Long orderId, Order.OrderStatus status) {
        Order order = getById(orderId);
        order.setStatus(status);
        return orderRepository.save(order);
    }
}
