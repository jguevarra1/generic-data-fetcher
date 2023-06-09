package com.datafetcher.service

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import jakarta.inject.Singleton

// TODO replace println calls in this class with Logging

/** The class containing the methods that upload a file to an Amazon S3 bucket */
@Singleton
class AmazonService
{
    /** The AWSCredentials object */
    static AWSCredentials credentials

    /**
     * uploadFileAmazon - Uploads a given file to a given path with given credentials to an Amazon S3 bucket
     *
     * @param accessKey - The accessKey to S3
     * @param secretKey - The secretKey to S3
     * @param configName - The name shown after upload in S3
     * @param filePath - The filepath to the file that will be uploaded
     * @param region - The region of the S3 bucket
     * @param bucketName - The name of the bucket being uploaded to
     */
    static void uploadFileAmazon(String accessKey, String secretKey, String configuration_name, String filepath, String region, String bucket_name) {
        credentials = new BasicAWSCredentials(accessKey, secretKey)

        // build and connects to the S3 client
        AmazonS3 s3client = AmazonS3ClientBuilder
        .standard()
        .withCredentials(new AWSStaticCredentialsProvider(credentials))
        .withRegion(region)
        .build()

        //uploading object
        s3client.putObject(bucket_name, configuration_name + ".csv", new File(filepath.toString()))
        printf("Completed upload to S3\n")
    }
}