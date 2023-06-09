package com.datafetcher.controller

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import jakarta.inject.Inject
import com.datafetcher.service.ZipFileRequirements


@Controller("/zipFiles")
class ZipFileController {
    @Inject
    ZipFileRequirements zipFileRequirement;


    @Get(produces = MediaType.APPLICATION_JSON)
    String sendData() {
        return zipFileRequirement.getFileRequirementsJson();
    }
}
