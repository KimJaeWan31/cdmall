package com.demo.cdmall1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.*;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.*;
import org.springframework.security.config.annotation.web.builders.*;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.crypto.password.*;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.demo.cdmall1.security.*;

import lombok.*;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Order(1)
	@Configuration
	public static class AdminSecurityConfig extends WebSecurityConfigurerAdapter {
		@Autowired
		private PasswordEncoder passwordEncoder;
		@Autowired
		private AdminLoginSuccessHandler adminLoginSuccessHandler;
		@Autowired
		private AdminLoginFailureHandler adminLoginFailureHandler;
		@Autowired
		private AccessDeniedHandler accessDeniedHandler;
		
		
		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.inMemoryAuthentication()
				.withUser("SYSTEM01")
				.password(passwordEncoder.encode("12341234!"))
				.roles("ADMIN");
		}

		@Override 
		protected void configure(HttpSecurity http) throws Exception { 
			
			http.authorizeRequests()
				.antMatchers("/admin/login")
				.permitAll();
			
			http.requestMatchers().antMatchers("/admin/**").and().authorizeRequests().anyRequest().hasRole("ADMIN")
			.and().exceptionHandling().accessDeniedHandler(accessDeniedHandler)
			.and().formLogin().loginPage("/admin/login").loginProcessingUrl("/admin/login")
			.successHandler(adminLoginSuccessHandler).failureHandler(adminLoginFailureHandler)
			.and().logout().logoutUrl("/member/logout").logoutSuccessUrl("/").invalidateHttpSession(true);
		}
	}
	
	@Order(2)
	@RequiredArgsConstructor
	@Configuration
	@EnableGlobalMethodSecurity(prePostEnabled=true, securedEnabled=true)	
	public static class MemberSecurityConfig extends WebSecurityConfigurerAdapter{
		
		@Autowired
		private PasswordEncoder passwordEncoder;
		@Autowired
		private LoginSuccessHandler memberLoginSuccessHandler;
		@Autowired
		private LoginFailureHandler memberLoginFailureHandler;
		
		// 권한이 틀리면 403 오류가 발생. 403 오류를 처리하는 핸들러
		@Autowired
		private AccessDeniedHandler accessDeniedHandler;
		
		@Autowired
		private final CustomUserDetailsService service;

		/*
		 * @Override protected void configure(AuthenticationManagerBuilder auth) throws
		 * Exception {
		 * auth.inMemoryAuthentication().withUser("spring").password(passwordEncoder.
		 * encode("1234")).roles("USER"); }
		 */
		
		/*
		 * @Override protected void configure(AuthenticationManagerBuilder auth) throws
		 * Exception {
		 * auth.inMemoryAuthentication().withUser("spring").password(passwordEncoder.
		 * encode("1234")).roles("USER"); }
		 */
		
		@Bean
		public DaoAuthenticationProvider getAuthenticationProvider() {
			DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
			daoAuthenticationProvider.setUserDetailsService(service);
			daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
			return daoAuthenticationProvider;
		}
		
		
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.csrf().disable().exceptionHandling().accessDeniedHandler(accessDeniedHandler);
				
			http.formLogin().loginPage("/member/login").loginProcessingUrl("/member/login")
				.usernameParameter("username").passwordParameter("password")
				.successHandler(memberLoginSuccessHandler).failureHandler(memberLoginFailureHandler);
			
			http.logout().logoutUrl("/member/logout").logoutSuccessUrl("/").invalidateHttpSession(true);
		}
		
	}
}


