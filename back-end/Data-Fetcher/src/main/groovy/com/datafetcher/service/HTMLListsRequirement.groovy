package com.datafetcher.service

import com.datafetcher.service.Requirements
import com.datafetcher.service.AmazonRequirement
import jakarta.inject.Singleton;
import jakarta.inject.Inject

@Singleton
class HTMLListsRequirement extends Requirements {

    @Inject
    AmazonRequirement amazonRequirement

    Map fileTypeProperties() {
        return [URL:"url", selector: 'text', list_index: 'text', headers: "text"]
    }

    Map storageProperties() {
        return amazonRequirement.getStorage();
    }
}