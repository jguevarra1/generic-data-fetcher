package com.datafetcher.controller

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import jakarta.inject.Inject
import com.datafetcher.service.HTMLTablesRequirements


@Controller("/htmlTables")
class TableController {
    @Inject
    HTMLTablesRequirements tableRequirement;


    @Get(produces = MediaType.APPLICATION_JSON)
    String sendData() {
        return tableRequirement.getFileRequirementsJson();
    }
}
