package test.isim.sample.aws.security;

import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import test.isim.sample.aws.category.ComponentTest;
import test.isim.sample.aws.factory.TestS3PolicyStatementFactory;
import test.isim.sample.aws.resource.CredentialsResourceProvider;

import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.auth.policy.Policy;
import com.amazonaws.auth.policy.Statement;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClient;
import com.amazonaws.services.securitytoken.model.Credentials;
import com.amazonaws.services.securitytoken.model.GetFederationTokenRequest;
import com.amazonaws.services.securitytoken.model.GetFederationTokenResult;

@Category(ComponentTest.class)
public class TemporarySecurityTokenTest {
  private static final int TOKEN_TIME_TO_LIVE = 7200; // secs
  
  private String testBucket = "ivan-test-bucket";
  private AWSSecurityTokenServiceClient stsClient;
  private AmazonS3 fixtureClient;
  
  @Before
  public void setUp() throws IOException{
    testBucket = "ivan-test-bucket";
    fixtureClient = new AmazonS3Client(CredentialsResourceProvider.loadCredentialsResource());
    fixtureClient.createBucket(testBucket);
    
    stsClient = new AWSSecurityTokenServiceClient(CredentialsResourceProvider.loadCredentialsResource());
  }

  @After
  public void tearDown(){
    if(fixtureClient.doesBucketExist(testBucket))
      fixtureClient.deleteBucket(testBucket);
  }
  
  /**
   * @see http://docs.amazonwebservices.com/AmazonS3/latest/dev/AuthUsingTempFederationTokenJava.html
   */
  @Test
  public void testSecurity_GenerateTemporaryToken_ToListBuckets(){
    GetFederationTokenRequest tokenRequest = generateTokenRequestToListBuckets();
    GetFederationTokenResult tokenResult = stsClient.getFederationToken(tokenRequest);
    BasicSessionCredentials basicSessionCredentials = createBasicSessionCredentials(tokenResult);
    
    // temporary token added to session credentials
    AmazonS3 temporaryClient = new AmazonS3Client(basicSessionCredentials);
    temporaryClient.listBuckets();
  }

  private GetFederationTokenRequest generateTokenRequestToListBuckets() {
    GetFederationTokenRequest tokenRequest = new GetFederationTokenRequest();
    tokenRequest.setDurationSeconds(TOKEN_TIME_TO_LIVE);
    tokenRequest.setName("test-user");
    
    String resourceARN = "arn:aws:s3:::*";
    Statement allowListBucketsStatement = 
      TestS3PolicyStatementFactory.createAllowListBucketsPolicyStatement(resourceARN);
    Policy policy = new Policy();
    policy.withStatements(allowListBucketsStatement);
    
    tokenRequest.setPolicy(policy.toJson());
    return tokenRequest;
  }
  
  /**
   * @see http://docs.amazonwebservices.com/AmazonS3/latest/dev/AuthUsingTempFederationTokenJava.html
   */
  @Test
  public void testSecurity_GenerateTemporaryToken_ToListObjectsInBuckets(){
    GetFederationTokenRequest tokenRequest = generateTokenRequestToListObjects();
    GetFederationTokenResult tokenResult = stsClient.getFederationToken(tokenRequest);
    BasicSessionCredentials basicSessionCredentials = createBasicSessionCredentials(tokenResult);
    
    // temporary token added to session credentials
    AmazonS3Client temporaryClient = new AmazonS3Client(basicSessionCredentials);
    Assert.assertNotNull(temporaryClient.listObjects(testBucket));
    temporaryClient.shutdown();
  }

  private GetFederationTokenRequest generateTokenRequestToListObjects() {
    GetFederationTokenRequest tokenRequest = new GetFederationTokenRequest();
    tokenRequest.setDurationSeconds(TOKEN_TIME_TO_LIVE);
    tokenRequest.setName("test-user");
    
    String bucketARN = "arn:aws:s3:::" + testBucket;
    Statement allowListObjectsStatement = 
      TestS3PolicyStatementFactory.createAllowListObjectsPolicyStatement(bucketARN);
    Policy policy = new Policy();
    policy.withStatements(allowListObjectsStatement);
    
    tokenRequest.setPolicy(policy.toJson());
    return tokenRequest;
  }
  
  private BasicSessionCredentials createBasicSessionCredentials(GetFederationTokenResult tokenResult) {
    Credentials sessionCredentials = tokenResult.getCredentials();
    BasicSessionCredentials basicSessionCredentials = new BasicSessionCredentials(
       sessionCredentials.getAccessKeyId(),
       sessionCredentials.getSecretAccessKey(),
       sessionCredentials.getSessionToken());
    return basicSessionCredentials;
  }
}
