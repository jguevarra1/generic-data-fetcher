package com.datafetcher.controller

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import jakarta.inject.Inject
import com.datafetcher.service.TextFileRequirement
import com.datafetcher.service.GDriveRequirement


@Controller("/googleDrive")
class GDriverController {

    @Inject
    GDriveRequirement googleDriveRequirement

    @Get(produces = MediaType.APPLICATION_JSON)
    String sendData() {
        return googleDriveRequirement.getRequirements();
    }
}
