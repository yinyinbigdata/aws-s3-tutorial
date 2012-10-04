package test.isim.sample.aws.bucket;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

public class DeleteBucketTest {
  private String awsAccessKeyID;
  private String awsSecretKey;
  private String bucketName;
  private AWSCredentials credentials;
  private AmazonS3 s3client;
  
  @Before
  public void setUp(){
    awsAccessKeyID = "";
    awsSecretKey = "";
    credentials = new BasicAWSCredentials(awsAccessKeyID, awsSecretKey);
    s3client = new AmazonS3Client(credentials);
    bucketName = "ivan-test-bucket";
  }

  @Test
  public void testGetAllBuckets_ListNotNull() {
    s3client.createBucket(bucketName);
    s3client.deleteBucket(bucketName);
    Assert.assertTrue(!s3client.doesBucketExist(bucketName));
  }

}
