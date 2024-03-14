package com.jhnr.myclub.services.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.jhnr.myclub.config.aws.AWSConfig;
import com.jhnr.myclub.services.AmazonClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Service
@Slf4j
public class AmazonClientServiceImpl implements AmazonClientService {

    private AmazonS3 amazonS3;

    @Value("${amazonProperties.bucketName}")
    private String bucketName;
    @Value("${amazonProperties.accessKey}")
    private String accessKey;
    @Value("${amazonProperties.secretKey}")
    private String secretKey;
    @Value("${amazonProperties.endpointS3Url}")
    private String endpointS3Url;

    public AmazonClientServiceImpl(AWSConfig awsConfig) {
        this.amazonS3 = awsConfig.amazonS3();
    }

    @Override
    public void putObject(String fileName, File file) {
        try {
            var putObjectRequest = new PutObjectRequest(this.bucketName, fileName, file).withCannedAcl(
                    CannedAccessControlList.PublicRead);
            this.amazonS3.putObject(putObjectRequest);
        } catch (Exception e){
            log.error("Some error has ocurred.");
        }

    }

    @Override
    public List<S3ObjectSummary> listObjects(String userName){
        ObjectListing objectListing = this.amazonS3.listObjects(this.bucketName, userName + "/");
        return objectListing.getObjectSummaries();
    }

    @Override
    public String getURLFromObjectKey(String objectKey, String userName) {
        if (!objectKey.startsWith(userName + "/")) {
            log.error("User cant load this image, don't send data to client");
            return "";
        }
        return "https://" + this.bucketName + "." + this.endpointS3Url + "/" + objectKey;
    }

    @Override
    public void deleteMultipleObjects(List<String> objects){
        DeleteObjectsRequest delObjectsRequests = new DeleteObjectsRequest(bucketName)
                .withKeys(objects.toArray(new String[0]));
        this.amazonS3.deleteObjects(delObjectsRequests);
    }


}
