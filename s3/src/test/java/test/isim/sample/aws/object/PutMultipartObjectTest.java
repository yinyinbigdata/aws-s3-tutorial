package test.isim.sample.aws.object;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import test.isim.sample.aws.bucket.CreateBucketTest;

import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;


public class PutMultipartObjectTest {
  private AmazonS3Client s3client;
  private TransferManager transferManager;
  private String bucketName = "ivan-test-bucket";
  private String objectKey = "ivan-test-object";
  private String fileName = "ivan-test-file";
  
  @Before
  public void setUp() throws IOException{
    initS3ClientWithCredentials();
    initTransferManagerWithCredentials();
    initFixtures();
  }

  private void initS3ClientWithCredentials() throws IOException {
    PropertiesCredentials credentials = new PropertiesCredentials(CreateBucketTest.class.getResourceAsStream("/credentials.properties"));
    s3client = new AmazonS3Client(credentials);
  }
  
  private void initTransferManagerWithCredentials() throws IOException {
    transferManager = new TransferManager(new PropertiesCredentials(CreateBucketTest.class.getResourceAsStream("/credentials.properties")));
  }
  
  private void initFixtures() {
    createTestBucketFixture();
    generateDistinctObjectKey();
    generateDistinctFileName();
  }
  
  private void createTestBucketFixture() {
    s3client.createBucket(bucketName);
  }
  
  private void generateDistinctObjectKey() {
    objectKey = objectKey.concat("-").concat(Long.toString(new Date().getTime())); 
  }
  
  private void generateDistinctFileName() {
    fileName = fileName.concat("-").concat(Long.toString(new Date().getTime()));
  }

  @After
  public void tearDown(){
    s3client.deleteObject(bucketName, objectKey);
    if(s3client.doesBucketExist(bucketName))
      s3client.deleteBucket(bucketName);
  }
  
  @Test
  public void testSynchronousMultipartUpload_ObjectNotNull(){
    File file = new File(fileName);
    Upload upload = transferManager.upload(bucketName, objectKey, file);
    try {
      upload.waitForCompletion();
      Assert.assertNotNull(s3client.getObject(bucketName, objectKey));
    } catch (Exception e) {
      e.printStackTrace();
      Assert.fail("Unexpected exception encountered while waiting for upload to complete");
    }
  }
  
  @Test
  public void testSynchronousMultipartUpload_ObjectContentNotNull(){
    File file = new File(fileName);
    Upload upload = transferManager.upload(bucketName, objectKey, file);
    try {
      upload.waitForCompletion();
      S3Object uploadedObject = s3client.getObject(bucketName, objectKey);
      Assert.assertNotNull(uploadedObject.getObjectContent());
    } catch (Exception e) {
      e.printStackTrace();
      Assert.fail("Unexpected exception encountered while waiting for upload to complete");
    }
  }
}
