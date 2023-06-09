package com.datafetcher.service

import java.nio.file.Path
import groovy.json.JsonSlurper
import io.micronaut.core.beans.BeanIntrospection
import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.apache.commons.io.FilenameUtils
import org.apache.commons.io.FileUtils

/**
 * The class containing the methods to create our database for this application
 */
@Singleton
class DB
{
    /** Used to create an instance of JSONWriterService */
    def introspection = BeanIntrospection.getIntrospection(JSONWriterService)
    JSONWriterService output = introspection.instantiate()

    /** In-memory database */
    ArrayList<Form> inMemoryDB = output.updateDB()

    /** Creates a JsonSlurper object to parse JSON */
    JsonSlurper jsonParser = new JsonSlurper()

    /** Create an instance of the DownloadService to interact with */
    @Inject
    DownloadService downloadService

    /** Create an instance of the ConverterService to interact with */
    @Inject
    ConverterService converterService

    /** Create an instance of the AmazonService to interact with */
    @Inject
    AmazonService amazonService

    /** Create an instance of the GoogleDriveService to interact with */
    @Inject
    GoogleDriveService googleDriveService

    /**
     * add - Creates a form object from the String JSON version of the form that was sent from the front-end
     *       This uses the JsonSlurper to parse the String to extract it's properties to create a form object.
     *       It calls our services to
     *          1. Download a file to our local disk (at the path that was specified in the front-end).
     *          2. Convert the file into CSV format and UTF-8 encoding.
     *          3. Upload the converted CSV file into either AmazonS3 or Google Drive.
     *
     *       Once this process is completed, the form object is added to our in-memory database.
     *
     * @param formAsString The form sent from the front end as a String JSON
     */
    void add(String formAsString)
    {
        def parsedForm = jsonParser.parseText(formAsString)
        Form form = new Form(parsedForm.configName, parsedForm.fileType,
                parsedForm.storage, parsedForm.updateFrequency, parsedForm.parseInfo, parsedForm.status, parsedForm.description)

        try
        {
            downloadService.downloadFile(form.getUrlToDownload(), form.parseInfoProperties.getLocalPath())

            String filePath = downloadService.getLocalPathSaved()

            if (downloadService.isZip(filePath))
            {
                filePath = FilenameUtils.removeExtension(filePath) + "/" + form.parseInfoProperties.getFileName()
            }

            String csvFileLocation = converterService.renameFile((filePath))

            converterService.readFile(Path.of(filePath), form.parseInfoProperties.getDelimiter() as char, form.getFileType(),
                    form.parseInfoProperties.getIndex(), form.parseInfoProperties.getHeaders(),
                    form.parseInfoProperties.getSelector(), form.parseInfoProperties.getListIndexes())

            if (form.storageChoice == "Amazon S3")
            {

                // TODO: We could refactor this
                String pathSlash = form.amazonS3Properties.pathToFolder

                if ((pathSlash.charAt(pathSlash.length() - 1)) != '/')
                {
                    String pathSlashs = pathSlash + "/"

                    amazonService.uploadFileAmazon(form.amazonS3Properties.getAccessKey(), form.amazonS3Properties.getSecretKey(), pathSlashs+form.getConfigName(),
                            csvFileLocation, form.amazonS3Properties.getRegion(), form.amazonS3Properties.getBucketName())
                }
                else
                {
                    amazonService.uploadFileAmazon(form.amazonS3Properties.getAccessKey(), form.amazonS3Properties.getSecretKey(),
                            form.amazonS3Properties.pathToFolder + form.getConfigName(), csvFileLocation,
                            form.amazonS3Properties.getRegion(), form.amazonS3Properties.getBucketName())
                }
            }

            else
            {
                googleDriveService.uploadFileGoogle(form.getConfigName(), csvFileLocation, form.googleDriveProperties.folderName)
            }


            form.setStatus('success');
        } catch (e) {
            form.setStatus('error');
            println(e);
        }

        FileUtils.cleanDirectory(new File(form.parseInfoProperties.getLocalPath()))
        inMemoryDB.add(form)

        // sort by timestamp to maintain insertion order
        inMemoryDB.sort{it.timeStamp}
        output.writeToDB(inMemoryDB)
    }

    /**
     * getForm - Retrieves a specific form object given a UUID and returns it
     *
     * @param id - The id to search for (in String form)
     * @return Returns the form that matches the given UUID
     */
    Form getForm(UUID id)
    {
        for (Form form in inMemoryDB)
        {
            String uuidStr = form.id.toString()
            if ((UUID.fromString(uuidStr)) == id)
            {
                return form
            }
        }
        return null
    }
    /**
     * getFormAsString - Retrieves a specific form given a UUID and returns it as a String JSON
     *
     * @param id - The id to search for (in String form)
     * @return Returns form that matches the given UUID in String JSON format or null if not found
     */
    String getFormAsString(UUID id) {
        for (form in inMemoryDB) {
            def str = form.id.toString()
            if ((UUID.fromString(str) <=> id) == 0) {
                return output.beautify(form)
            }
        }
        return null
    }

    /**
     * getDBAsString - Returns a String JSON formatted representation of all entries in current database.
     *
     * @return All entries in database as a String JSON format.
     */
    String getDBAsString()
    {
        this.inMemoryDB = output.updateDB()
        return output.beautify(inMemoryDB)
    }

    /**
     * getDB - Retrieves all entries in database as an immutable list
     * @return Returns all entries in database as  a list
     */
    List<Form> getDB()
    {
        return this.inMemoryDB.asImmutable()
    }
}
