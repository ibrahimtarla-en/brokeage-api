package com.brokeage.api.controller;

import com.brokeage.api.dto.BRApiResponse;
import com.brokeage.api.model.Asset;
import com.brokeage.api.service.AssetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assets")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Asset API", description = "Endpoints for listing customer assets")
public class AssetController {

    private final AssetService assetService;

    @GetMapping
    @SecurityRequirement(name = "basicAuth")
    @Operation(summary = "List assets by customer", description = "Lists all assets for a given customer")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Assets successfully retrieved"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - authentication required")
    })
    public ResponseEntity<BRApiResponse<List<Asset>>> listAssets(@RequestParam Long customerId) {
        List<Asset> assets = assetService.listAssetsByCustomer(customerId);
        return ResponseEntity.ok(BRApiResponse.success("Assets listed", assets));
    }
}
