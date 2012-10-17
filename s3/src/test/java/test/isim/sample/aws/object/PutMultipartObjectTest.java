package test.isim.sample.aws.object;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import test.isim.sample.aws.category.ComponentTest;
import test.isim.sample.aws.factory.TestS3ObjectFactory;
import test.isim.sample.aws.resource.CredentialsResourceProvider;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;

@Category(ComponentTest.class)
public class PutMultipartObjectTest {
  private static final int DEFAULT_BYTE_STREAM_SIZE = 1114950; // about 1M
  private AmazonS3Client s3client;
  private TransferManager transferManager;
  private String bucketName = "ivan-test-bucket";
  private String objectKey = "ivan-test-object";
  private InputStream inputContent;
  private ObjectMetadata metadata;
  
  @Before
  public void setUp() throws IOException{
    initS3ClientWithCredentials();
    initTransferManagerWithCredentials();
    initFixtures();
  }

  private void initS3ClientWithCredentials() throws IOException {
    s3client = new AmazonS3Client(CredentialsResourceProvider.loadCredentialsResource());
  }
  
  private void initTransferManagerWithCredentials() throws IOException {
    transferManager = new TransferManager(CredentialsResourceProvider.loadCredentialsResource());
  }
  
  private void initFixtures() {
    createTestBucketFixture();
    generateDistinctObjectKey();
    generateInputContent();
    generateObjectMetadata();
  }
  
  private void createTestBucketFixture() {
    s3client.createBucket(bucketName);
  }
  
  private void generateDistinctObjectKey() {
    objectKey = objectKey.concat("-").concat(Long.toString(new Date().getTime())); 
  }
  
  private void generateInputContent(){
    inputContent = TestS3ObjectFactory.createTestInputContent();
  }
  
  private void generateObjectMetadata(){
    metadata = TestS3ObjectFactory.createDefaultSmallObjectMetadata();
  }
  
  @After
  public void tearDown() throws IOException{
    s3client.deleteObject(bucketName, objectKey);
    if(s3client.doesBucketExist(bucketName))
      s3client.deleteBucket(bucketName);
    if(inputContent != null)
      inputContent.close();
  }
  
  @Test
  public void testSynchronousMultipartUpload_ObjectNotNull(){
    Upload upload = transferManager.upload(bucketName, objectKey, inputContent, metadata);
    
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
    Upload upload = transferManager.upload(bucketName, objectKey, inputContent, metadata);
    
    try {
      upload.waitForCompletion();
      S3Object uploadedObject = s3client.getObject(bucketName, objectKey);
      Assert.assertNotNull(uploadedObject.getObjectContent());
    } catch (Exception e) {
      e.printStackTrace();
      Assert.fail("Unexpected exception encountered while waiting for upload to complete");
    }
  }
  
  @Test
  public void testSynchronousMultipartUpload_ContentLengthMatches(){
    s3client.putObject(bucketName, objectKey, inputContent, metadata);
    
    try {
      S3Object uploadedObject = s3client.getObject(bucketName, objectKey);
      int expectedContentSize = inputContent.read(new byte[DEFAULT_BYTE_STREAM_SIZE]);
      int actualUploadedContentSize = uploadedObject.getObjectContent().read(new byte[DEFAULT_BYTE_STREAM_SIZE]);
      Assert.assertEquals(expectedContentSize, actualUploadedContentSize);
    } catch (IOException e) {
      e.printStackTrace();
      Assert.fail("Unexpected I/O exception");
    }
  }
}
