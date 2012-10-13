package test.isim.sample.aws.security;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.ExpectedException;

import test.isim.sample.aws.category.ComponentTest;
import test.isim.sample.aws.resource.CredentialsResourceProvider;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

@Category(ComponentTest.class)
public class SimpleIAMSecurityTest {
  private AmazonS3 s3client;
  private ExpectedException expectedException = ExpectedException.none();

  @Test
  public void testValidUserCredentials_ShouldSucceed() throws IOException{
    s3client = new AmazonS3Client(CredentialsResourceProvider.loadCredentialsResource());
    Assert.assertNotNull(s3client.listBuckets());
  }
  
  @Test
  public void testInvalidUserCredentials_ShouldFail(){
    String fakeAccessKey = "a-fake-access-key";
    String fakeSecretKey = "a-fake-secret-key";
    AWSCredentials fakeCredentials = new BasicAWSCredentials(fakeAccessKey, fakeSecretKey);
    try {
      s3client = new AmazonS3Client(fakeCredentials);
      s3client.listBuckets();
      Assert.fail("Expected exception did not occur");
    } catch(Exception e) {
      expectedException.expect(AmazonServiceException.class);
      expectedException.expectMessage("Error Code: InvalidAccessKeyId");
    }
  }
}
