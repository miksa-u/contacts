package miksa.contacts.data;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import miksa.contacts.BaseTest;
import miksa.contacts.config.AppConfig;

public class ContactRepoAwsMockTest extends BaseTest {

	private ContactRepoAws contactRepo = new ContactRepoAws(new AppConfig(), null, null);
	
	@Test 
	public void formatS3FileNameToContact() {
		assertThat(contactRepo.formatS3FileNameToContact("user", "user/file"), equalTo("file"));
	}

	@Test 
	public void formatContactToS3FileName() {
		assertThat(contactRepo.formatContactToS3FileName("user", "file"), equalTo("user/file"));
	}

}
