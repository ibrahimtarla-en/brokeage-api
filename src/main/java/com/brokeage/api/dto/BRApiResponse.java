package com.brokeage.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Schema(description = "Generic API response wrapper")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BRApiResponse<T> {

    @Schema(description = "Operation result status")
    private boolean success;

    @Schema(description = "Informational or error message")
    private String message;

    @Schema(description = "Response payload")
    private T data;

    @Schema(description = "Timestamp of the response")
    private LocalDateTime timestamp;

    public static <T> BRApiResponse<T> success(String message, T data) {
        return new BRApiResponse<>(true, message, data, LocalDateTime.now());
    }

    public static <T> BRApiResponse<T> error(String message) {
        BRApiResponse<T> response = new BRApiResponse<>();
        response.success = false;
        response.message = message;
        response.data = null;
        return response;
    }

    public static <T> BRApiResponse<T> success(String message) {
        return success(message, null);
    }

    public static <T> BRApiResponse<T> success(T data) {
        return success("Success", data);
    }

    public static <T> BRApiResponse<T> failure(String message) {
        return new BRApiResponse<>(false, message, null, LocalDateTime.now());
    }
}
