package test.isim.sample.aws.resource;

import java.io.IOException;

import com.amazonaws.auth.PropertiesCredentials;

public class CredentialsResourceProvider {
  public static PropertiesCredentials loadCredentialsResource() throws IOException{
    return new PropertiesCredentials(CredentialsResourceProvider.class.getResourceAsStream("/credentials.properties"));
  }
}
