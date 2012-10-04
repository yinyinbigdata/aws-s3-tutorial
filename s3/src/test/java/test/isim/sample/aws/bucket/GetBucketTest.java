package test.isim.sample.aws.bucket;

import java.io.IOException;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

public class GetBucketTest {
  private AmazonS3 s3client;
  
  @Before
  public void setUp() throws IOException{
    initS3ClientWithCredentials();
  }
  
  private void initS3ClientWithCredentials() throws IOException {
    PropertiesCredentials credentials = new PropertiesCredentials(CreateBucketTest.class.getResourceAsStream("/credentials.properties"));
    s3client = new AmazonS3Client(credentials);
  }

  @Test
  public void testGetAllBuckets_ListNotNull() {
    Assert.assertNotNull(s3client.listBuckets());
  }
}
