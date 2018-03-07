package miksa.contacts.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

@Configuration
public class AppConfig {

	@Value("${aws.s3.bucket}")
	public String s3Bucket;
	
	@PostConstruct
	public void postConstruct() {
		Assert.notNull(s3Bucket, "s3Bucket is null");
	}
	
}
