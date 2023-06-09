package com.datafetcher.controller

import com.datafetcher.service.AmazonService
import com.datafetcher.service.DB
import com.datafetcher.service.DownloadService
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import jakarta.inject.Inject

/** Controller that handles calls that require interaction with the database. */
@Controller("/db")
class DBController
{
    /** Create an instance of our database to interact with. */
    @Inject
    DB database

    /** Create an instance of DownloadService to interact with. */
    @Inject
    DownloadService downloadService

    /** Create an instance of AmazonService to interact with. */
    @Inject
    AmazonService amazonService

    /**
     * sendData - Handles call to retrieve all form objects in our database for display in the front-end.
     *
     * @return JSON representation of all entries in db.
     */
    @Get(produces = MediaType.APPLICATION_JSON)
    String sendData() {
        database.getDBAsString()
    }

    /**
     * sendForm - Handles call to retrieve a specific form object in our database.
     *
     * @param id UUID of the item.
     * @return JSON representation of the item.
     */
    @Get("/{id}")
    String sendForm(@PathVariable UUID id)
    {
        return database.getFormAsString(id)
    }

    /**
     * saveForm - Handles call to create another configuration and store the form into our in-memory database.
     *
     * @param The new configuration form to add to the database
     * @return The status code of the creation
     */
    @Post
    HttpResponse<String> saveForm(@Body String form)
    {
        println(form);
        database.add(form)
        return HttpResponse.ok(form)
    }
}