package miksa.contacts.web;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.mock.web.MockHttpServletRequest;

import miksa.contacts.BaseTest;

@AutoConfigureMockMvc
public class AuthRequestWrapperTest extends BaseTest  {

	private static final String HEADER_VALUE = "BASIC";

	private AuthRequestWrapper authRequest;
	private MockHttpServletRequest request;
	private Enumeration<String> enumeration;
	private String result;

	@Before
	public void setUp() {
		request = new MockHttpServletRequest();
		authRequest = new AuthRequestWrapper(request);
	}

	@Test
	public void getHeader_getUserHeader() throws Exception {
		request.addHeader(AuthRequestWrapper.USER_HEADER, HEADER_VALUE);

		result = authRequest.getHeader(AuthRequestWrapper.USER_HEADER);
		
		assertThat(result, equalTo(HEADER_VALUE));
	}

	@Test
	public void getHeader_authPresent() throws Exception {
		request.addHeader(AuthRequestWrapper.AUTH_HEADER, HEADER_VALUE);

		result = authRequest.getHeader(AuthRequestWrapper.AUTH_HEADER);
		
		assertThat(result, equalTo(HEADER_VALUE));
	}

	@Test
	public void getHeader_authNotPresent() throws Exception {
		result = authRequest.getHeader(AuthRequestWrapper.AUTH_HEADER);
		
		assertNull(result);
	}

	@Test
	public void getHeader_userPresent() throws Exception {
		request.addHeader(AuthRequestWrapper.USER_HEADER, HEADER_VALUE);

		result = authRequest.getHeader(AuthRequestWrapper.AUTH_HEADER);
		
		assertThat(result, equalTo(HEADER_VALUE));
	}

	@Test
	public void getHeaders_userPresent() throws Exception {
		request.addHeader(AuthRequestWrapper.USER_HEADER, HEADER_VALUE);

		enumeration = authRequest.getHeaders(AuthRequestWrapper.AUTH_HEADER);
		
		List<String> headers = Collections.list(enumeration);
		assertThat(headers.size(), equalTo(1));
		assertThat(headers.get(0), equalTo(HEADER_VALUE));
	}

	@Test
	public void getHeaderNames_authPresent() throws Exception {
		request.addHeader(AuthRequestWrapper.AUTH_HEADER, HEADER_VALUE);

		enumeration = authRequest.getHeaderNames();
		
		assertTrue(enumeration.hasMoreElements());
		assertThat(enumeration.nextElement(), equalTo(AuthRequestWrapper.AUTH_HEADER));
	}

	@Test
	public void getHeaderNames_authNotPresent() throws Exception {
		enumeration = authRequest.getHeaderNames();
		
		assertFalse(enumeration.hasMoreElements());
	}

	@Test
	public void getHeaderNames_userPresent() throws Exception {
		request.addHeader(AuthRequestWrapper.USER_HEADER, HEADER_VALUE);

		enumeration = authRequest.getHeaderNames();
		
		List<String> headers = Collections.list(enumeration);
		assertThat(headers.size(), equalTo(2));
		assertThat(headers.get(0), equalTo(AuthRequestWrapper.USER_HEADER));
		assertThat(headers.get(1), equalTo(AuthRequestWrapper.AUTH_HEADER));
	}

}
