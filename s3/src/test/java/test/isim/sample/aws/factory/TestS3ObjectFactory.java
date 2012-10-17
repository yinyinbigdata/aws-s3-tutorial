package test.isim.sample.aws.factory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Date;

import com.amazonaws.services.s3.model.ObjectMetadata;

public class TestS3ObjectFactory {
  private static String DEFAULT_ENCODING = "UTF-8";
  private static long DEFAULT_CONTENT_SIZE_IN_BYTES = 2048;
  
  public static ObjectMetadata createDefaultObjectMetadata(){
    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentEncoding(DEFAULT_ENCODING);
    metadata.setContentLength(DEFAULT_CONTENT_SIZE_IN_BYTES);
    metadata.setLastModified(new Date());
    return metadata;
  }
  
  public static InputStream createTestInputContent(){
    byte[] inputContent = new byte[2048];
    InputStream inputStream = new ByteArrayInputStream(inputContent);
    return inputStream;
  }
}
