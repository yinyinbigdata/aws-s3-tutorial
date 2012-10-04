package test.isim.sample.aws.factory;

import com.amazonaws.auth.policy.Resource;
import com.amazonaws.auth.policy.Statement;
import com.amazonaws.auth.policy.actions.S3Actions;

public class TestS3PolicyStatementFactory {
  /**
   * @see http://docs.amazonwebservices.com/AWSJavaSDK/latest/javadoc/com/amazonaws/auth/policy/actions/S3Actions.html
   * @see http://docs.amazonwebservices.com/AWSJavaSDK/latest/javadoc/com/amazonaws/auth/policy/Resource.html
   */
  public static Statement createAllowAllActionsPolicyStatement(String resourceARN){
    Statement statement = new Statement(Statement.Effect.Allow);
    statement.withActions(S3Actions.AllS3Actions);
    statement.withResources(new Resource(resourceARN));
    return statement;
  }
  
  public static Statement createAllowListBucketsPolicyStatement(String resourceARN){
    Statement statement = new Statement(Statement.Effect.Allow);
    statement.withActions(S3Actions.ListBuckets);
    statement.withResources(new Resource(resourceARN));
    return statement;
  }
  
  public static Statement createAllowListObjectsPolicyStatement(String resourceARN){
    Statement statement = new Statement(Statement.Effect.Allow);
    statement.withActions(S3Actions.ListObjects);
    statement.withResources(new Resource(resourceARN));
    return statement;
  }
}
