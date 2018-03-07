package miksa.contacts.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;


public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

      chain.doFilter(getAuthRequest(request), response);

    }

		private ServletRequest getAuthRequest(ServletRequest request) {
			if (request instanceof HttpServletRequest) {
				return (ServletRequest) new AuthRequestWrapper((HttpServletRequest) request);
			}
			return request;
		}

		@Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // NOOP
    }

    @Override
    public void destroy() {
        // NOOP
    }
}
