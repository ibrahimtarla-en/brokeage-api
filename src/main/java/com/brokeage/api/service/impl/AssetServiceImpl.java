package com.brokeage.api.service.impl;

import com.brokeage.api.model.Asset;
import com.brokeage.api.repository.AssetRepository;
import com.brokeage.api.service.AssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AssetServiceImpl implements AssetService {

    private final AssetRepository assetRepository;

    @Override
    public List<Asset> listAssetsByCustomer(Long customerId) {
        return assetRepository.findAllByCustomerId(customerId);
    }
}
