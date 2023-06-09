package com.datafetcher.service

import com.datafetcher.service.Requirements
import com.datafetcher.service.AmazonRequirement
import jakarta.inject.Singleton;
import jakarta.inject.Inject

@Singleton
class HTMLTablesRequirements extends Requirements {

    @Inject
    AmazonRequirement amazonRequirement

    Map fileTypeProperties() {
        return [URL:"url", table_index: 'number', headers: "text"]
    }

    Map storageProperties() {
        return amazonRequirement.getStorage();
    }
}