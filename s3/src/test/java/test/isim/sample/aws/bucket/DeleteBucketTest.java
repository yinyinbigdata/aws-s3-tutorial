package test.isim.sample.aws.bucket;

import java.io.IOException;
import java.util.Date;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import test.isim.sample.aws.category.ComponentTest;
import test.isim.sample.aws.resource.CredentialsResourceProvider;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

@Category(ComponentTest.class)
public class DeleteBucketTest {
  private String bucketName = "ivan-test-bucket";
  private AmazonS3 s3client;
  
  @Before
  public void setUp() throws Exception{
    initS3ClientWithCredentials();
    generateDistinctBucketName();
  }
  
  private void initS3ClientWithCredentials() throws IOException {
    s3client = new AmazonS3Client(CredentialsResourceProvider.loadCredentialsResource());
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
