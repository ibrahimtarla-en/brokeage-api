package com.brokeage.api.dto;

import com.brokeage.api.model.OrderSide;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderRequest {

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotBlank(message = "Asset name must not be blank")
    private String assetName;

    @NotNull(message = "Order side is required")
    private OrderSide orderSide;

    @Min(value = 1, message = "Order size must be at least 1")
    private double size;

    @Min(value = 0, message = "Price must be non-negative")
    private double price;
}
