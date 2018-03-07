package miksa.contacts.web;

import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthRequestWrapper extends HttpServletRequestWrapper {

		public static final String AUTH_HEADER = "authorization";
    public static final String USER_HEADER = "app-key";

    private static final Logger LOG = LoggerFactory.getLogger(AuthRequestWrapper.class);

    public AuthRequestWrapper(HttpServletRequest request) {
			super(request);
		}

    @Override
    public String getHeader(String name) {
    	String header = super.getHeader(name);
    	LOG.debug("getHeader: {}: {}", name, header);

    	if (! AUTH_HEADER.equals(name.toLowerCase()) || header != null) {
    		return header;
    	}
			
			String userHeader = super.getHeader(USER_HEADER);
    	LOG.debug("getHeader: added {}: {}", AUTH_HEADER, userHeader);
			return userHeader;
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
    	Enumeration<String> headers = super.getHeaders(name);
    	boolean hasElements = headers.hasMoreElements();
    	LOG.debug("getHeaders: {}: {}", name, hasElements);

			if (! AUTH_HEADER.equals(name.toLowerCase()) || hasElements) {
    		return super.getHeaders(name);
    	}
			
			List<String> newHeaders = Collections.list(super.getHeaders(name));
			String userHeader = super.getHeader(USER_HEADER);
			newHeaders.add(userHeader);
    	LOG.debug("getHeaders: added {}: {}", AUTH_HEADER, userHeader);

    	return Collections.enumeration(newHeaders);
    }

    @Override
    public Enumeration<String> getHeaderNames() {
  		Enumeration<String> headerNames = super.getHeaderNames();
    	if (super.getHeader(AUTH_HEADER) != null) {
      	LOG.debug("getHeaderNames: {} found", AUTH_HEADER);
				return headerNames;
    	}
    	
    	if (super.getHeader(USER_HEADER) == null) {
      	LOG.debug("getHeaderNames: {} is null", USER_HEADER);
				return headerNames;
    	}
    	
    	List<String> newHeaderNames = Collections.list(headerNames);
    	LOG.debug("getHeaderNames: added {}", AUTH_HEADER);
     	newHeaderNames.add(AUTH_HEADER);
    	
    	return Collections.enumeration(newHeaderNames);
    }
}
