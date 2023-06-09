package com.datafetcher.service

import io.micronaut.core.annotation.Introspected

// TODO potentially encrypt secretKey and accessKey
// TODO refactor the stuff inside parseInfoProperties. Too many if statements
// TODO still need to fix gdrive properties
/**
 * The class and subclasses to create Form objects to store user's config info.
 * The UUID is automatically and randomly created.
 *
 * Uses the @Introspected annotation so that it can be used in other classes within Micronaut
 */
@Introspected
class Form
{
    /** Subclass to store Amazon S3 Credentials */
    class AmazonS3Properties
    {
        /** The access key of the S3 account */
        String accessKey

        /** The secret key of the S3 account */
        String secretKey

        /** The name of the bucket in the S3 account */
        String bucketName

        /** The region location */
        String region

        /** The path to store to in the S3 bucket */
        String pathToFolder

        /**
         * Constructor to initialize the AmazonS3Properties object.
         *
         * @param storageInfo - The storage information from the form
         */
        AmazonS3Properties(Object storageInfo)
        {
            this.accessKey = storageInfo.access_key
            this.secretKey = storageInfo.secret_key
            this.bucketName = storageInfo.bucket_name
            this.region = storageInfo.region
            this.pathToFolder = storageInfo.path_to_folder
        }
    }

    /** Subclass to store Google Drive credentials */
    class GoogleDriveProperties
    {
        /** The folder to save the file to */
        String folderName

        /**
         * Constructor to initialize the GoogleDriveProperties object.
         *
         * @param storageInfo - The storage information from the form
         */
        GoogleDriveProperties(Object storageInfo)
        {
            this.folderName = storageInfo.folder_name
        }
    }

    /** Subclass to store the information needed to parse and convert files */
    class ParseInfoProperties
    {
        /** The name of the file to parse (for zip files) */
        String fileName

        /** The delimiter of the file (for text files) */
        char delimiter

        /** The headers for each column in the file */
        ArrayList<String> headers

        /** The index of the table to parse (for HTML Tables */
        int index

        /** The indexes of the lists to parse (for HTML lists) */
        ArrayList<Integer> listIndexes = new ArrayList<Integer>()

        /** The path location to store to in local disk */
        String localPath

        /** The selector to find the HTML Unordered list to parse */
        String selector

        /**
         * Constructor to initialize the ParseInfoProperties object.
         *
         * @param fileInfo - The file information sent from the front-end.
         */
        ParseInfoProperties(def fileInfo) {
            this.fileName = fileInfo.file_name;

            if (fileInfo.delimiter != null) {
                if (!fileInfo.delimiter.isEmpty())
                {
                    if (fileInfo.delimiter == "\\t")
                    {
                        delimiter = '\t' as char
                    }
                }
            }

            if (fileInfo.table_index != null)
            {
                this.index = fileInfo.table_index as int;
            }

            if (fileInfo.list_index != null)
            {
                for (String index in fileInfo.list_index.split(", "))
                {
                    if (index != null)
                    {
                        listIndexes.add(Integer.parseInt(index))
                    }
                }
            }

            this.headers = List.of(fileInfo.headers.split("\\s*,\\s*"))

            this.localPath = "./Downloads"

            if (fileInfo.selector != null)
            {
                this.selector = fileInfo.selector
            }

            else selector = ""
        }
    }

    /** The name of the configuration */
    String configName

    /** The type of file that is being downloaded and converted */
    String fileType

    /** The URL to download from */
    String urlToDownload

    /** The storage choice */
    String storageChoice

    /** The update frequency for scheduler threads (not implemented) */
    String updateFrequency

    //TODO is this used??
    HashSet<String> listOfFiles

    /** The ParseInfoProperties object */
    ParseInfoProperties parseInfoProperties

    /** The AmazonS3Properties object */
    AmazonS3Properties amazonS3Properties

    /** The GoogleDriveProperties object */
    GoogleDriveProperties googleDriveProperties

    /** The time stamp that the form was created */
    String timeStamp

    /** The status of the form (success, updating, or error) */
    String status

    /** The description of the configuration form */
    String description

    /** The unique ID assigned to the form object */
    UUID id

    /**
     * getTimeStamp - Generates the time that the form was created at
     * @return Returns the date that the form was created as a String
     */
    static String getTimeStamp()
    {
        return new Date() as String
    }

    /**
     * Constructor to initialize the form object
     *
     * @param configName - The name of the configuration
     * @param fileType - The type of file that is being parsed and converted
     * @param storage - The storage information object from the front-end
     * @param updateFrequency - The update frequency for scheduler threads (not implemented)
     * @param parseInfo - The parse information object from the front-end
     * @param status - The status of the form (success, updating, or error)
     * @param description - The description of the configuration form
     */
    Form(String configName, String fileType, def storage, String updateFrequency, def parseInfo, String status, String description) {
        this.configName = configName
        this.fileType = fileType
        this.id = UUID.randomUUID()
        this.storageChoice = storage.storageName
        this.updateFrequency = updateFrequency
        this.urlToDownload = parseInfo.listOfFiles[0].URL

        // Check storage type
        if (storage.storageName == 'Amazon S3') {
            this.amazonS3Properties = new AmazonS3Properties(storage.storageInfo)
        }

        if (storage.storageName == 'Google Drive') {
            this.googleDriveProperties = new GoogleDriveProperties(storage.storageInfo)
        }

        this.parseInfoProperties = new ParseInfoProperties(parseInfo.listOfFiles[0])
        this.timeStamp = getTimeStamp()
        this.status = status
        this.description = description
    }
}
