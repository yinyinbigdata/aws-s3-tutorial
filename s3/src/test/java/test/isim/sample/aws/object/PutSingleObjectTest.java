package test.isim.sample.aws.object;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import test.isim.sample.aws.bucket.CreateBucketTest;

import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3Object;


public class PutSingleObjectTest {
  private AmazonS3Client s3client;
  private String bucketName = "ivan-test-bucket";
  private String objectKey = "ivan-test-object";
  private String fileName = "ivan-test-file";
  
  @Before
  public void setUp() throws IOException{
    initS3ClientWithCredentials();
    createTestBucketFixture();
    generateDistinctObjectKey();
    generateDistinctFileName();
  }

  @After
  public void tearDown(){
    s3client.deleteObject(bucketName, objectKey);
    if(s3client.doesBucketExist(bucketName))
      s3client.deleteBucket(bucketName);
  }

  private void initS3ClientWithCredentials() throws IOException {
    PropertiesCredentials credentials = new PropertiesCredentials(CreateBucketTest.class.getResourceAsStream("/credentials.properties"));
    s3client = new AmazonS3Client(credentials);
  }
  
  private void createTestBucketFixture() {
    s3client.createBucket(bucketName);
  }
  
  private void generateDistinctObjectKey() {
    objectKey = objectKey.concat("-").concat(Long.toString(new Date().getTime())); 
  }
  
  private void generateDistinctFileName() {
    fileName = fileName.concat("_").concat(Long.toString(new Date().getTime()));
  }

  @Test
  public void testPutObject_SingleObjectUpload_NotNull(){
    File file = new File(fileName);
    s3client.putObject(bucketName, objectKey, file);
    S3Object uploadedObject = s3client.getObject(bucketName, objectKey);
    Assert.assertNotNull(uploadedObject);
  }
  
  @Test
  public void testPutObject_SingleObjectUpload_ContentNotNull(){
    File file = new File(fileName);
    s3client.putObject(bucketName, objectKey, file);
    S3Object uploadedObject = s3client.getObject(bucketName, objectKey);
    Assert.assertNotNull(uploadedObject.getObjectContent());
  }
}