package com.sugarcorner.service;

import com.sugarcorner.dto.OrderItemRequest;
import com.sugarcorner.model.entity.Product;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@SessionScope
public class CartService {

    private final Map<Long, Integer> items = new ConcurrentHashMap<>();
    private final ProductService productService;

    public CartService(ProductService productService) {
        this.productService = productService;
    }

    public void addItem(Long productId, int quantity) {
        items.merge(productId, quantity, Integer::sum);
    }

    public void removeItem(Long productId) {
        items.remove(productId);
    }

    public void clear() {
        items.clear();
    }

    public Map<Long, Integer> getItems() {
        return new java.util.HashMap<>(items);
    }

    public List<OrderItemRequest> getOrderItems() {
        List<OrderItemRequest> result = new ArrayList<>();
        items.forEach((productId, qty) -> result.add(new OrderItemRequest(productId, qty)));
        return result;
    }

    public BigDecimal getTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (Map.Entry<Long, Integer> e : items.entrySet()) {
            Product p = productService.getById(e.getKey());
            total = total.add(p.getPrice().multiply(BigDecimal.valueOf(e.getValue())));
        }
        return total;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }
}
