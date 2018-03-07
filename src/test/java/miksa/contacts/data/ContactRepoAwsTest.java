package miksa.contacts.data;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import miksa.contacts.BaseSpringTest;

public class ContactRepoAwsTest extends BaseSpringTest {

	private static final String CONTACT_DATA = "data";
	private static final String CONTACT_NAME = "_test_";

	@Autowired
	private ContactRepo contactRepo;
	
	@Test
	public void saveLoadFindDelete() {
		Contact contact = new Contact(CONTACT_NAME, CONTACT_DATA);

		contactRepo.save(username, contact);
		
		Contact result = contactRepo.load(username, CONTACT_NAME);
		
		assertThat(result, equalTo(contact));

		List<Contact> contacts = contactRepo.findAll(username);
		
		assertThat(contacts.size(), greaterThan(0));
		//assertThat(contacts.get(0), equalTo(contact));
		assertTrue(contacts.stream().anyMatch(item -> item.name.equals(contact.name)));

		contactRepo.delete(username, CONTACT_NAME);
	}
}
