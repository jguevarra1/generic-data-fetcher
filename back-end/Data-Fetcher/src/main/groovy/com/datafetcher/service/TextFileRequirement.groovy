package com.datafetcher.service

import com.datafetcher.service.Requirements
import com.datafetcher.service.AmazonRequirement
import jakarta.inject.Singleton;
import jakarta.inject.Inject

@Singleton
class TextFileRequirement extends Requirements {

    @Inject
    AmazonRequirement amazonRequirement

    Map fileTypeProperties() {
        return [URL:"url", delimiter: "text", headers: "text"]
    }

    Map storageProperties() {
        return amazonRequirement.getStorage();
    }
}