package test.isim.sample.aws;

import java.util.Date;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import test.isim.sample.aws.factory.TestAccessControlGrantPermissionFactory;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CreateBucketRequest;

public class CreateBucketTest {
  private String newBucketName = "ivan-test-bucket";
  private String awsAccessKeyID;
  private String awsSecretKey;
  private AWSCredentials credentials;
  private AmazonS3 s3client;
  private ExpectedException expectedException = ExpectedException.none();
  
  @Before
  public void setUp(){
    awsAccessKeyID = "";
    awsSecretKey = "";
    credentials = new BasicAWSCredentials(awsAccessKeyID, awsSecretKey);
    s3client = new AmazonS3Client(credentials);
    
    Date now = new Date();
    newBucketName = newBucketName.concat("-").concat(new Long(now.getTime()).toString());
  }

  @After
  public void tearDown(){
    if(s3client.doesBucketExist(newBucketName))
      s3client.deleteBucket(newBucketName);
  }
  
  @Test
  public void testCreateBucket_NewBucketNotNull() {
    Bucket bucket = s3client.createBucket(newBucketName);
    Assert.assertNotNull(bucket);
  }
  
  @Test
  public void testCreateBucket_NewBucketExist(){
    s3client.createBucket(newBucketName);
    Assert.assertTrue(s3client.doesBucketExist(newBucketName));
  }
  
  @Test
  public void testCreateBucket_NewBucketNameMatches(){
    Bucket bucket = s3client.createBucket(newBucketName);
    Assert.assertEquals(bucket.getName(),newBucketName);
  }
  
  @Test
  public void testCreateBucket_DuplicatesNoEffect(){
    s3client.createBucket(newBucketName);
    int listSizeBeforeDuplication = s3client.listBuckets().size();
    s3client.createBucket(newBucketName);
    int listSizeAfterDuplication = s3client.listBuckets().size();
    Assert.assertTrue(listSizeBeforeDuplication == listSizeAfterDuplication);
  }

  /**
   * For this to work, all e-mails have to be legitimate
   * @see http://docs.amazonwebservices.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/s3/model/Grantee.html
   */
  @Test
  public void testCreateBucket_AccessControlWithBadEmails(){
    CreateBucketRequest createRequest = new CreateBucketRequest(newBucketName);
    AccessControlList aclist = new AccessControlList();
    aclist.grantAllPermissions(
      TestAccessControlGrantPermissionFactory.grantReadPermission("user1@boeing.com"),
      TestAccessControlGrantPermissionFactory.grantWritePermission("user2@boeing.com"),
      TestAccessControlGrantPermissionFactory.grantFullPermission("user3@boeing.com")
    );
    createRequest.setAccessControlList(aclist);
    
    try {
      s3client.createBucket(createRequest);
    } catch(Exception e){
      expectedException.expect(AmazonServiceException.class);
      expectedException.expectMessage("The e-mail address you provided does not match any account on record.");
    }
  }
}
