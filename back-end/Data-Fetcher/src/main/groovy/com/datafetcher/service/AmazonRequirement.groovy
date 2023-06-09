package com.datafetcher.service

import com.datafetcher.service.Requirements
import jakarta.inject.Singleton;

@Singleton
class AmazonRequirement extends Requirements {

    // Nothing to return since it's for Amazon storage specifically
    Map fileTypeProperties() {
        return [:]
    }

    public Map storageProperties() {
        return [secret_key: 'password', access_key: 'password', bucket_name: 'text', region: 'text', path_to_folder: 'text']
    }
}