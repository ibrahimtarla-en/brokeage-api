package com.brokeage.api.service.impl;

import com.brokeage.api.dto.OrderRequest;
import com.brokeage.api.exception.BusinessException;
import com.brokeage.api.model.Asset;
import com.brokeage.api.model.Order;
import com.brokeage.api.model.OrderSide;
import com.brokeage.api.model.OrderStatus;
import com.brokeage.api.repository.AssetRepository;
import com.brokeage.api.repository.OrderRepository;
import com.brokeage.api.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static com.brokeage.api.constant.ErrorMessages.*;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final AssetRepository assetRepository;

    @Transactional
    @Override
    public Order createOrder(OrderRequest req) {
        Long customerId = req.getCustomerId();
        String assetName = req.getAssetName();
        OrderSide side = req.getOrderSide();

        if (side == OrderSide.BUY) {
            Asset tryAsset = assetRepository.findByCustomerIdAndAssetName(customerId, "TRY")
                    .orElseThrow(() -> new BusinessException(TRY_ASSET_NOT_FOUND));

            double totalCost = req.getSize() * req.getPrice();
            if (tryAsset.getUsableSize() < totalCost) {
                throw new BusinessException(INSUFFICIENT_TRY);
            }

            tryAsset.setUsableSize(tryAsset.getUsableSize() - totalCost);
            assetRepository.save(tryAsset);
        }

        if (side == OrderSide.SELL) {
            Asset asset = assetRepository.findByCustomerIdAndAssetName(customerId, assetName)
                    .orElseThrow(() -> new BusinessException(ASSET_NOT_FOUND));

            if (asset.getUsableSize() < req.getSize()) {
                throw new BusinessException(INSUFFICIENT_ASSET);
            }

            asset.setUsableSize(asset.getUsableSize() - req.getSize());
            assetRepository.save(asset);
        }

        Order order = Order.builder()
                .customerId(customerId)
                .assetName(assetName)
                .orderSide(side)
                .size(req.getSize())
                .price(req.getPrice())
                .status(OrderStatus.PENDING)
                .createDate(LocalDateTime.now())
                .build();

        return orderRepository.save(order);
    }

    @Override
    public List<Order> listOrders(Long customerId, LocalDate start, LocalDate end) {
        return orderRepository.findAllByCustomerIdAndCreateDateBetween(
                customerId,
                start.atStartOfDay(),
                end.atTime(LocalTime.MAX)
        );
    }

    @Transactional
    @Override
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException(ORDER_NOT_FOUND));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new BusinessException(ONLY_PENDING_CAN_BE_CANCELLED);
        }

        order.setStatus(OrderStatus.CANCELED);

        if (order.getOrderSide() == OrderSide.BUY) {
            Asset tryAsset = assetRepository.findByCustomerIdAndAssetName(order.getCustomerId(), "TRY")
                    .orElseThrow(() -> new BusinessException(TRY_ASSET_NOT_FOUND));

            double refund = order.getSize() * order.getPrice();
            tryAsset.setUsableSize(tryAsset.getUsableSize() + refund);
            assetRepository.save(tryAsset);
        }

        if (order.getOrderSide() == OrderSide.SELL) {
            Asset asset = assetRepository.findByCustomerIdAndAssetName(order.getCustomerId(), order.getAssetName())
                    .orElseThrow(() -> new BusinessException(ASSET_NOT_FOUND));

            asset.setUsableSize(asset.getUsableSize() + order.getSize());
            assetRepository.save(asset);
        }

        orderRepository.save(order);
    }
}
