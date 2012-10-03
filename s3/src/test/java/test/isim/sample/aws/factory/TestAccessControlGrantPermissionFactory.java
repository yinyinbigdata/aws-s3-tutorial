package test.isim.sample.aws.factory;

import com.amazonaws.services.s3.model.EmailAddressGrantee;
import com.amazonaws.services.s3.model.Grant;
import com.amazonaws.services.s3.model.Grantee;
import com.amazonaws.services.s3.model.Permission;

public class TestAccessControlGrantPermissionFactory {
  public static Grant grantReadPermission(String email){
    Grantee emailGrantee = new EmailAddressGrantee(email);
    return new Grant(emailGrantee, Permission.Read);
  }
  
  public static Grant grantWritePermission(String email){
    Grantee emailGrantee = new EmailAddressGrantee(email);
    return new Grant(emailGrantee, Permission.Write);
  }
  
  public static Grant grantFullPermission(String email){
    Grantee emailGrantee = new EmailAddressGrantee(email);
    return new Grant(emailGrantee, Permission.FullControl);
  }
}
