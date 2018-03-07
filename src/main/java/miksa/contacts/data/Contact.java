package miksa.contacts.data;

import org.springframework.util.Assert;

public class Contact implements Comparable<Contact> {

	public String name;
	public String data;

	public Contact() {}

	public Contact(String name, String data) {
		Assert.notNull(name, "name");
		Assert.notNull(data, "data");
		this.name = name;
		this.data = data;
	}

	@Override
	public boolean equals(Object targetObj) {
		if (targetObj == null || ! (targetObj instanceof Contact)) {
			return false;
		}
		Contact target = (Contact) targetObj;
		return name.equals(target.name) && data.equals(target.data);
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public String toString() {
		return String.format("Contact: { name: '%s', data: '%s' }", name, data);
	}

	@Override
	public int compareTo(Contact obj) {
		if (obj == null) {
			return 1;
		}
		return name.compareTo(obj.name);
	}
}
