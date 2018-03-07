package miksa.contacts.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import miksa.contacts.data.Contact;
import miksa.contacts.data.ContactRepo;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {

	private ContactRepo contactRepo;

	@Autowired
	public ContactController(ContactRepo contactRepo) {
		this.contactRepo = contactRepo;
	}

	@DeleteMapping(value="/{name}")
	public ResponseEntity<Status> delete(@PathVariable String name) {
		contactRepo.delete(getAuthUser(), name);
		return new ResponseEntity<Status>(new Status("OK", name + " deleted"), HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<List<Contact>> findAll() {
		return new ResponseEntity<List<Contact>>(contactRepo.findAll(getAuthUser()), HttpStatus.OK);
	}

	@GetMapping(value="/{name}")
	public ResponseEntity<Contact> load(@PathVariable String name) {
		return new ResponseEntity<Contact>(contactRepo.load(getAuthUser(), name), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<Status> save(@RequestBody Contact contact) {
		contactRepo.save(getAuthUser(), contact);
		return new ResponseEntity<Status>(new Status("OK", contact.name + " saved"), HttpStatus.OK);
	}

	public static class Status {
		public String status;
		public String message;
		
		public Status(String status, String message) {
			this.status = status;
			this.message = message;
		}
	}

	/*
	 * Private
	 */
	private String getAuthUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Assert.isTrue(authentication.isAuthenticated(), "User is not Authenticated");
		String user = authentication.getName();
		return user;
	}

}
