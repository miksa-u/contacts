package miksa.contacts;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
public class ContactsApplicationTest extends BaseSpringTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Test
	public void actuatorHealth() throws Exception {
		mockMvc.perform(get("/actuator/health").with(basicAuth()))
			//.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().string(containsString("UP")))
			.andExpect(jsonPath("status").value("UP"))
		;
	}

}
