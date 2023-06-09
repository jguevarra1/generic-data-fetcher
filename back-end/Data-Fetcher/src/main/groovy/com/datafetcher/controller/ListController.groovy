package com.datafetcher.controller

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import jakarta.inject.Inject
import com.datafetcher.service.HTMLListsRequirement


@Controller("/htmlLists")
class ListController {
    @Inject
    HTMLListsRequirement listRequirement;


    @Get(produces = MediaType.APPLICATION_JSON)
    String sendData() {
        return listRequirement.getFileRequirementsJson();
    }
}
