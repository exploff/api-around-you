package com.jhnr.myclub.services;

import com.amazonaws.services.s3.model.S3ObjectSummary;

import java.io.File;
import java.util.List;

public interface AmazonClientService {

    public void putObject(String fileName, File file);

    public List<S3ObjectSummary> listObjects(String userName);

    public void deleteMultipleObjects(List<String> objects);

    public String getURLFromObjectKey(String objectKey, String userName);

}