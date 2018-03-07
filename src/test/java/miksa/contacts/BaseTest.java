package miksa.contacts;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.rules.TestName;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseTest {

	public Logger LOG = LoggerFactory.getLogger(this.getClass());
	
	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	@Rule
	public TestName testName = new TestName();

	@Before
	public void baseSetUp() {
		LOG.info("Test Name: {}", testName.getMethodName());
	}
}
