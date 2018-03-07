package miksa.contacts.data;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.WritableResource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3;

import miksa.contacts.config.AppConfig;

@Service
public class ContactRepoAws implements ContactRepo {

	private static final String ALL = "*";
	private static final String EMPTY = "";
	private static final String S3_URL_TEMPLATE = "s3://%s/%s/%s";

	private Logger LOG = LoggerFactory.getLogger(ContactRepoAws.class);
	
  private AmazonS3 amazonS3;
	private String bucket;
	private ResourcePatternResolver resourcePatternResolver;

	@Autowired
	public ContactRepoAws(AppConfig config, AmazonS3 amazonS3, ResourcePatternResolver resourcePatternResolver) {
		this.amazonS3 = amazonS3;
		this.bucket = config.s3Bucket;
		this.resourcePatternResolver = resourcePatternResolver;
		LOG.info("AWS bucket: {}", this.bucket);
	}


	@Override
	public void delete(String user, String name) {
		String resourceName = formatContactToS3FileName(user, name);
		LOG.info("delete {}", resourceName);
		try {
			amazonS3.deleteObject(bucket, resourceName);
		} catch(Exception ex) {
			throw new RuntimeException("Cannot delete Contact.name: " + name, ex);
		}
	}

	@Override
	public List<Contact> findAll(String user) {
		String bucketKey = getObjectURI(user, ALL);
		List<Contact> contacts = new LinkedList<>();

		try {
			Resource[] resources = resourcePatternResolver.getResources(bucketKey);
			for (Resource resource : resources) {
				contacts.add(new Contact(formatS3FileNameToContact(user, resource.getFilename()), EMPTY));
			}
		} catch(Exception ex) {
			throw new RuntimeException("Cannot find Contacts in: " + bucketKey, ex);
		}
		
		LOG.info(String.format("findAll: size: %,d",  contacts.size()));
		Collections.sort(contacts);
		return contacts;
	}

	@Override
	public Contact load(String user, String name) {
		LOG.info("load: {}", name);
		return loadContact(name, getResource(user, name));
	}

	@Override
	public Contact save(String user, Contact contact) {
		LOG.info("save {}", contact);
		Resource resource = null;
		try {
			resource = getResource(user, contact.name);
			WritableResource writableResource = (WritableResource) resource;
			try (OutputStream out = writableResource.getOutputStream()) {
				out.write(contact.data.getBytes());
			}
		} catch(Exception ex) {
			throw new RuntimeException(String.format("Cannot save: %s; resource: %s", contact, getDescription(resource)), ex);
		}
		
		return contact;
	}

	
	/*
	 * Private
	 */

	private String copyStreamToString(InputStream in) {
		try (Scanner scanner = new Scanner(in)) {
			scanner.useDelimiter("\\A");
			return scanner.hasNext() ? scanner.next() : EMPTY;
		}
	}

	protected String formatContactToS3FileName(String user, String filename) {
		return user + "/" + filename;
	}

	protected String formatS3FileNameToContact(String user, String filename) {
		return filename.replaceAll("^" + user + "/",  EMPTY);
	}

	private Object getDescription(Resource resource) {
		return resource == null ? null : resource.getDescription();
	}

	private String getObjectURI(String user, String name) {
		return String.format(S3_URL_TEMPLATE, bucket, user, name);
	}

	private Resource getResource(String user, String name) {
		return resourcePatternResolver.getResource(getObjectURI(user, name));
	}

	private Contact loadContact(String name, Resource resource) {
		try (InputStream in = resource.getInputStream()) {
			return new Contact(name, copyStreamToString(in));
		} catch(Exception ex) {
			throw new RuntimeException("Cannot load resource: " + getDescription(resource), ex);
		}
	}

}
