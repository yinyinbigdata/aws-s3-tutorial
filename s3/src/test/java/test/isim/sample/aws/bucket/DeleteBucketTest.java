package test.isim.sample.aws.bucket;

import java.io.IOException;
import java.util.Date;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

public class DeleteBucketTest {
  private String bucketName = "ivan-test-bucket";
  private AmazonS3 s3client;
  
  @Before
  public void setUp() throws Exception{
    initS3ClientWithCredentials();
    generateDistinctBucketName();
  }
  
  private void initS3ClientWithCredentials() throws IOException {
    PropertiesCredentials credentials = new PropertiesCredentials(CreateBucketTest.class.getResourceAsStream("/credentials.properties"));
    s3client = new AmazonS3Client(credentials);
  }
  
  private void generateDistinctBucketName() {
    Date now = new Date();
    bucketName = bucketName.concat("-").concat(new Long(now.getTime()).toString());
  }

  @Test
  public void testGetAllBuckets_ListNotNull() {
    s3client.createBucket(bucketName);
    s3client.deleteBucket(bucketName);
    Assert.assertTrue(!s3client.doesBucketExist(bucketName));
  }

}
