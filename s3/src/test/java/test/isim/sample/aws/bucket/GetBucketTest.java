package test.isim.sample.aws.bucket;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

public class GetBucketTest {
  private String awsAccessKeyID;
  private String awsSecretKey;
  private AWSCredentials credentials;
  private AmazonS3 s3client;
  
  @Before
  public void setUp(){
    awsAccessKeyID = "";
    awsSecretKey = "";
    credentials = new BasicAWSCredentials(awsAccessKeyID, awsSecretKey);
    s3client = new AmazonS3Client(credentials);
  }

  @Test
  public void testGetAllBuckets_ListNotNull() {
    Assert.assertNotNull(s3client.listBuckets());
  }
}
