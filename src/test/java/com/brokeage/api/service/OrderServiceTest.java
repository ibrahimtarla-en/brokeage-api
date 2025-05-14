package com.brokeage.api.service;

import com.brokeage.api.dto.OrderRequest;
import com.brokeage.api.model.Asset;
import com.brokeage.api.model.Order;
import com.brokeage.api.model.OrderSide;
import com.brokeage.api.model.OrderStatus;
import com.brokeage.api.repository.AssetRepository;
import com.brokeage.api.repository.OrderRepository;
import com.brokeage.api.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    private final OrderRepository orderRepository = mock(OrderRepository.class);
    private final AssetRepository assetRepository = mock(AssetRepository.class);
    private final OrderService orderService = new OrderServiceImpl(orderRepository, assetRepository);

    @Test
    void cancelOrder_shouldUpdateStatus() {
        Order order = new Order();
        order.setId(1L);
        order.setStatus(OrderStatus.PENDING);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        orderService.cancelOrder(1L);

        assertEquals(OrderStatus.CANCELED, order.getStatus());
        verify(orderRepository).save(order);
    }

    @Test
    void createOrder_shouldSaveOrder() {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setCustomerId(1L);
        orderRequest.setAssetName("AAPL");
        orderRequest.setOrderSide(OrderSide.BUY);
        orderRequest.setSize(10.0);
        orderRequest.setPrice(100.0);

        // TRY asset: yeterli bakiye olması gerekiyor
        Asset tryAsset = new Asset();
        tryAsset.setAssetName("TRY");
        tryAsset.setCustomerId(1L);
        tryAsset.setSize(2000.0);
        tryAsset.setUsableSize(2000.0);

        when(assetRepository.findByCustomerIdAndAssetName(1L, "TRY"))
                .thenReturn(Optional.of(tryAsset));

        when(assetRepository.findByCustomerIdAndAssetName(1L, "AAPL"))
                .thenReturn(Optional.of(new Asset())); // varsa update

        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Order createdOrder = orderService.createOrder(orderRequest);

        assertEquals(OrderStatus.PENDING, createdOrder.getStatus());
        verify(orderRepository).save(any(Order.class));
    }
}
