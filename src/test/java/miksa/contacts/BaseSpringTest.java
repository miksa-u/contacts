package miksa.contacts;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import miksa.contacts.web.AuthRequestWrapper;

@RunWith(SpringRunner.class)
@SpringBootTest
abstract public class BaseSpringTest {

	@Value("${spring.security.user.name}")
	public String username;
	@Value("${spring.security.user.password}")
	private String password;

	public RequestPostProcessor basicAuth() {
		return httpBasic(username, password);
	}

	public RequestPostProcessor basicAuthUser() {
		return new HttpUserBasicRequestPostProcessor(username, password);
	}

	private static class HttpUserBasicRequestPostProcessor implements RequestPostProcessor {
		private String headerValue;

		private HttpUserBasicRequestPostProcessor(String username, String password) {
			byte[] toEncode;
			try {
				toEncode = (username + ":" + password).getBytes("UTF-8");
			}
			catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
			this.headerValue = "Basic " + new String(Base64.getEncoder().encode(toEncode));
		}

		@Override
		public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
			request.addHeader(AuthRequestWrapper.USER_HEADER, this.headerValue);
			return request;
		}
	}
}
