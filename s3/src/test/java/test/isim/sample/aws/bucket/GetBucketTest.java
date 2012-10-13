package test.isim.sample.aws.bucket;

import java.io.IOException;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import test.isim.sample.aws.category.ComponentTest;
import test.isim.sample.aws.resource.CredentialsResourceProvider;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

@Category(ComponentTest.class)
public class GetBucketTest {
  private AmazonS3 s3client;
  
  @Before
  public void setUp() throws IOException{
    initS3ClientWithCredentials();
  }
  
  private void initS3ClientWithCredentials() throws IOException {
    s3client = new AmazonS3Client(CredentialsResourceProvider.loadCredentialsResource());
  }

  @Test
  public void testGetAllBuckets_ListNotNull() {
    Assert.assertNotNull(s3client.listBuckets());
  }
}
