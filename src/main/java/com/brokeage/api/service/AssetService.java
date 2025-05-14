package com.brokeage.api.service;

import com.brokeage.api.model.Asset;

import java.util.List;

public interface AssetService {
    List<Asset> listAssetsByCustomer(Long customerId);
}
