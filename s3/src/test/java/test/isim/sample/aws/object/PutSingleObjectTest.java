package test.isim.sample.aws.object;

import java.io.IOException;
import java.util.Date;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import test.isim.sample.aws.category.ComponentTest;
import test.isim.sample.aws.factory.TestS3ObjectFactory;
import test.isim.sample.aws.resource.CredentialsResourceProvider;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3Object;

@Category(ComponentTest.class)
public class PutSingleObjectTest {
  private AmazonS3Client s3client;
  private String bucketName = "ivan-test-bucket";
  private String objectKey = "ivan-test-object";
  
  @Before
  public void setUp() throws IOException{
    initS3ClientWithCredentials();
    initFixtures();
  }

  private void initFixtures() {
    createTestBucket();
    generateDistinctObjectKey();
  }

  private void initS3ClientWithCredentials() throws IOException {
    s3client = new AmazonS3Client(CredentialsResourceProvider.loadCredentialsResource());
  }
  
  private void createTestBucket() {
    s3client.createBucket(bucketName);
  }
  
  private void generateDistinctObjectKey() {
    objectKey = objectKey.concat("-").concat(Long.toString(new Date().getTime())); 
  }
  
  @After
  public void tearDown(){
    s3client.deleteObject(bucketName, objectKey);
    if(s3client.doesBucketExist(bucketName))
      s3client.deleteBucket(bucketName);
  }

  @Test
  public void testPutObject_SingleObjectUpload_NotNull(){
    s3client.putObject(bucketName, objectKey,
        TestS3ObjectFactory.createTestInputContent(),
        TestS3ObjectFactory.createDefaultObjectMetadata());
    S3Object uploadedObject = s3client.getObject(bucketName, objectKey);
    Assert.assertNotNull(uploadedObject);
  }
  
  @Test
  public void testPutObject_SingleObjectUpload_ContentNotNull(){
    s3client.putObject(bucketName, objectKey,
        TestS3ObjectFactory.createTestInputContent(),
        TestS3ObjectFactory.createDefaultObjectMetadata());
    S3Object uploadedObject = s3client.getObject(bucketName, objectKey);
    Assert.assertNotNull(uploadedObject.getObjectContent());
  }
}