package miksa.contacts.web;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import miksa.contacts.BaseSpringTest;
import miksa.contacts.data.Contact;
import miksa.contacts.web.ContactController.Status;

@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ContactControllerTest extends BaseSpringTest {

	private static final String CONTACTS_URL = "/api/contacts/";
	private static final String TEST_NAME = "_test2_";
	@Autowired
	private MockMvc mockMvc;

	private Contact contact = new Contact(TEST_NAME, "data test");
	private String body;

	@Before
	public void setUp() throws Exception {
		body = objToString(contact);
	}

	@Test
	public void t0_noAuth() throws Exception {
		mockMvc.perform(get(CONTACTS_URL))
			.andDo(print())
			.andExpect(status().is4xxClientError())
		;
	}

	@Test
	public void t0_userHeaderAuth() throws Exception {
		mockMvc.perform(get(CONTACTS_URL).with(basicAuthUser()))
			.andDo(print())
			.andExpect(status().isOk())
		;
	}

	@Test
	public void t1_findAll() throws Exception {
		mockMvc.perform(get(CONTACTS_URL).with(basicAuth()))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().string(startsWith("[")))
		;
	}

	@Test
	public void t2_save() throws Exception {
		Object expectedMessage = objToString(new Status("OK", contact.name + " saved"));

		mockMvc.perform(post(CONTACTS_URL)
			.contentType(MediaType.APPLICATION_JSON)
			.content(body).with(basicAuth()))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().string(equalTo(expectedMessage)))
		;
	}

	@Test
	public void t3_load() throws Exception {
		mockMvc.perform(get(CONTACTS_URL + contact.name).with(basicAuth()))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().string(equalTo(body)))
		;
	}

	@Test
	public void t3_findExact() throws Exception {
		mockMvc.perform(get(CONTACTS_URL).with(basicAuth()))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().string(containsString("{\"name")))
		;
	}

	@Test
	public void t4_delete() throws Exception {
		Object expectedMessage = objToString(new Status("OK", contact.name + " deleted"));

		mockMvc.perform(MockMvcRequestBuilders.delete(CONTACTS_URL + contact.name).with(basicAuth()))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().string(equalTo(expectedMessage)))
		;
	}

	private String objToString(Object value) throws JsonProcessingException {
		return new ObjectMapper().writeValueAsString(value);
	}

}
