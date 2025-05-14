package com.brokeage.api.repository;

import com.brokeage.api.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByCustomerIdAndCreateDateBetween(Long customerId, LocalDateTime start, LocalDateTime end);
}
