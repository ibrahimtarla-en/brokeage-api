package com.brokeage.api.service;

import com.brokeage.api.dto.OrderRequest;
import com.brokeage.api.model.Order;

import java.time.LocalDate;
import java.util.List;

public interface OrderService {
    Order createOrder(OrderRequest req);

    List<Order> listOrders(Long customerId, LocalDate start, LocalDate end);

    void cancelOrder(Long orderId);
}
