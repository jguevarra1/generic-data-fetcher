package com.datafetcher.controller

import com.datafetcher.service.DownloadService
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import jakarta.inject.Inject

@Controller("/download")

/** Test controller responsible for handling calls to our DownloadService */
class DownloadController
{
    /** Create an instance of our DownloadService to interact with */
    @Inject
    DownloadService downloadService

    /**
     * download - The test method to download a file to a local disk path.
     *
     * Modify the parameters, make calls to other methods and add links to the links.txt file in the resources folder
     * to test the DownloadService methods
     *
     * The code gets ran at localhost:8080/download
     */
    @Get
    void download()
    {
        HashSet<String> urlList
        String filePath = new File("").getAbsolutePath()
        urlList = downloadService.getURLS(filePath)
        downloadService.downloadFiles(urlList, "./src/main/Downloads")
    }
}
