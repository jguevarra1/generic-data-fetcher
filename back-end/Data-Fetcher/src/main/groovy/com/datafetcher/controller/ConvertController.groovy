package com.datafetcher.controller

import com.datafetcher.service.ConverterService
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import jakarta.inject.Inject

/** Test controller responsible for handling calls to our ConverterService */
@Controller("/convert")
class ConvertController
{
    /** Create an instance of our ConverterService to interact with */
    @Inject
    ConverterService converterService

    /**
     * convert - The test method to convert a file to a CSV format with UTF-8 encoding in local disk.
     *
     * Insert methods you want to use and modify its parameters as needed.
     * The code gets ran at localhost:8080/convert
     */
    @Get
    void convert()
    {
       // Call converterService methods in here
       // converterService.readFile(String, char, String, int, List<String>, String)

    }
}
