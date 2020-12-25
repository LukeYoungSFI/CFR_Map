package com.sfi.cfrmap.config;

import javax.servlet.Filter;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;

import com.github.ziplet.filter.compression.CompressingFilter;
import com.sfi.cfrmap.filter.SSORequestHeaderAuthenticationFilter;
import com.sfi.cfrmap.security.OidUserDetailsService;
import com.sfi.cfrmap.uims.UimsClientUserInfoImpl;
import com.sfi.cfrmap.uims.UimsClientUserProfileImpl;

@Configuration
@ImportResource("classpath:META-INF/uims-context.xml")
@PropertySource("classpath:application.properties")
@EnableWebSecurity
@EnableTransactionManagement
@EnableCaching
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	private static final Logger logger = LoggerFactory.getLogger(SecurityConfiguration.class);
	@Autowired
	private Environment environment;

	// @Autowired
	// private UimsClientService uimsClientService;
	// to http://sdv-devlinjbs01/uimsws/UserInfoWS_v2
	@Autowired
	private UimsClientUserInfoImpl uimsClientUserInfo;

	// to http://sdv-devlinjbs01/uimsws/UserProfileWS_v2
	@Autowired
	private UimsClientUserProfileImpl uimsClientUserProfile;

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring()
				// Spring Security should completely ignore URLs starting with /resources/
				.antMatchers("/css/**", "/js/**", "/fonts/**", "/img/**", "/webjars/**", "/ico/**");
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {

		httpSecurity.authorizeRequests()
				// .expressionHandler(webExpressionHandler())
				.antMatchers("/error", "/invalid_user").permitAll().and()
				.addFilterBefore(ssoFilter(), RequestHeaderAuthenticationFilter.class)
				.authenticationProvider(preauthAuthProvider()).authorizeRequests().antMatchers("/views/**")
				.hasRole("ALL_ACCESS").anyRequest().authenticated().and().exceptionHandling().accessDeniedPage("/error")
				.and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/cfrmaplogout"))
				.logoutSuccessUrl("/index").and().exceptionHandling().accessDeniedPage("/error").and()
				.sessionManagement()// .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
				.maximumSessions(1).expiredUrl("/invalid_session").and().invalidSessionUrl("/invalid_user").and().csrf()
				.disable();
		httpSecurity.headers().frameOptions().disable();
		// .logout().logoutRequestMatcher(new
		// AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login");
	}

	@Autowired
	public void configureGloabl(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(preauthAuthProvider());
	}

	@Bean
	public SSORequestHeaderAuthenticationFilter ssoFilter() throws Exception {
		SSORequestHeaderAuthenticationFilter filter = new SSORequestHeaderAuthenticationFilter();
		filter.setExceptionIfHeaderMissing(false);
		filter.setAuthenticationManager(authenticationManager());
		return filter;
	}

	@Bean
	public AuthenticationProvider preauthAuthProvider() {
		PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
		provider.setPreAuthenticatedUserDetailsService(userDetailsServiceWrapper());
		provider.setThrowExceptionWhenTokenRejected(false);
		return provider;
	}

	@Bean
	public AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> userDetailsServiceWrapper() {
		UserDetailsByNameServiceWrapper<PreAuthenticatedAuthenticationToken> wrapper = new UserDetailsByNameServiceWrapper<>();
		wrapper.setUserDetailsService(new OidUserDetailsService(uimsClientUserInfo, uimsClientUserProfile));

		return wrapper;

	}

	@Bean
	public SpringSecurityDialect securityDialect() {
		return new SpringSecurityDialect();
	}

	@Bean
	public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {

		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setJsonPrefix(")]}',\n");

		return converter;
	}

	@Bean
	public Filter compressingFilter() {
		CompressingFilter compressingFilter = new CompressingFilter();
		return compressingFilter;
	}

	@Bean
	public HttpSessionListener httpSessionListener() {
		return new HttpSessionListener() {
			@Override
			public void sessionCreated(HttpSessionEvent hse) {
				logger.info("=== Session is Created === {}",
						SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
				// set the time to 30 min ie., (30 * 60)
				// hse.getSession().setMaxInactiveInterval(30 * 60);
			}

			@Override
			public void sessionDestroyed(HttpSessionEvent hse) {
				HttpSession httpSession = hse.getSession();
				SecurityContext securityContext = (SecurityContextImpl) httpSession
						.getAttribute("SPRING_SECURITY_CONTEXT");
				// logger.info("=== Session is destoryed === {}",
				// securityContext.getAuthentication().getPrincipal().toString());
			}
		};
	}

}
