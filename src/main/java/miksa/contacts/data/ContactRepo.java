package miksa.contacts.data;

import java.util.List;

public interface ContactRepo {
	
	void delete(String user, String name);

	List<Contact> findAll(String user);
	
	Contact load(String user, String name);

	Contact save(String user, Contact contact);

}
