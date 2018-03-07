package miksa.contacts.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;

import miksa.contacts.web.AuthFilter;
import miksa.contacts.web.RequestLogFilter;

@Configuration
@EnableWebSecurity(debug=false)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.cors().configurationSource(httpRequest -> {
			CorsConfiguration corsConfig = new CorsConfiguration();
			corsConfig.addAllowedOrigin(CorsConfiguration.ALL);
			corsConfig.addAllowedHeader(CorsConfiguration.ALL);
			for (HttpMethod method : HttpMethod.values()) {
				corsConfig.addAllowedMethod(method);
			}
			return corsConfig;
		});
		http
			.csrf().disable()
			.anonymous().disable()
			.authorizeRequests().antMatchers("/api/**").authenticated().and().httpBasic()
		;
	}
	
	@Bean
	public FilterRegistrationBean<AuthFilter> addAuthFilter() {
	    FilterRegistrationBean<AuthFilter> registration = new FilterRegistrationBean<>();
	    AuthFilter authFilter = new AuthFilter();
	    registration.setFilter(authFilter);
	    registration.addUrlPatterns("/api/*");
	    registration.setOrder(-1000);
	    return registration;
	}

	//@Bean
	public FilterRegistrationBean<RequestLogFilter> addRequestLogFilter() {
	    FilterRegistrationBean<RequestLogFilter> registration = new FilterRegistrationBean<>();
	    RequestLogFilter requestLogFilter = new RequestLogFilter();
	    registration.setFilter(requestLogFilter);
	    registration.addUrlPatterns("/api/*");
	    registration.setOrder(-500);
	    return registration;
	}
}
