package com.paperized.worldscrape.serviceImpl;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Service
public class AmazonClient {
  private final Logger logging = org.slf4j.LoggerFactory.getLogger(AmazonClient.class);
  private AmazonS3 s3client;

  @Value("${amazon.accessKey}")
  private String accessKey;
  @Value("${amazon.secretKey}")
  private String secretKey;
  @Value("${amazon.bucketName}")
  private String bucketName;
  @Value("${amazon.regionName}")
  private String regionName;

  @PostConstruct
  private void initializeAmazon() {
    AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
    s3client = AmazonS3ClientBuilder.standard()
                .withRegion(regionName)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
  }

  public void uploadFile(String fileName, String content) {
    InputStream stream = new ByteArrayInputStream(content.getBytes());
    s3client.putObject(new PutObjectRequest(bucketName, fileName, stream, null));
    logging.info("[ACTION] uploadFileS3: {}", fileName);
  }

  public void removeFile(String fileName) {
    s3client.deleteObject(new DeleteObjectRequest(bucketName, fileName));
    logging.info("[ACTION] removeFileS3: {}", fileName);
  }

  public String getStoragePath() {
    return "https://s3." + regionName + ".amazonaws.com/" + bucketName;
  }
}
