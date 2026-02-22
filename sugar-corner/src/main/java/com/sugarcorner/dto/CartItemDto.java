package com.sugarcorner.dto;

import com.sugarcorner.model.entity.Product;

import java.math.BigDecimal;

public record CartItemDto(Product product, int quantity, BigDecimal subtotal) {}
