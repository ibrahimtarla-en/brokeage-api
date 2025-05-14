package com.brokeage.api.controller;

import com.brokeage.api.dto.BRApiResponse;
import com.brokeage.api.dto.OrderRequest;
import com.brokeage.api.model.Order;
import com.brokeage.api.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Order API", description = "Endpoints for managing stock orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping

    @Operation(summary = "Create new order", description = "Creates a new buy or sell order for a customer")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Order successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid input or business rule violation"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - authentication required"),
            @ApiResponse(responseCode = "500", description = "Unexpected server error")
    })
    public ResponseEntity<BRApiResponse<Order>> createOrder(@RequestBody OrderRequest request) {
        Order order = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BRApiResponse.success("Order created", order));
    }

    @GetMapping

    @Operation(summary = "List orders", description = "Lists all orders for a customer in a date range")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Orders successfully retrieved"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - authentication required"),
            @ApiResponse(responseCode = "400", description = "Invalid date format or missing customer ID")
    })
    public ResponseEntity<BRApiResponse<List<Order>>> listOrders(
            @RequestParam Long customerId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end
    ) {
        List<Order> orders = orderService.listOrders(customerId, start, end);
        return ResponseEntity.ok(BRApiResponse.success("Order list", orders));
    }

    @DeleteMapping("/{orderId}")

    @Operation(summary = "Cancel order", description = "Cancels a pending order")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order cancelled"),
            @ApiResponse(responseCode = "400", description = "Order is not in pending status"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - authentication required"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ResponseEntity<BRApiResponse<Void>> cancelOrder(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
        return ResponseEntity.ok(BRApiResponse.success("Order cancelled"));
    }


}
