package com.myclass.sercurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@ComponentScan("com.myclass")
@Order(1)
public class WebSercurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private UserDetailsService userDetailsService;
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// TODO Auto-generated method stub
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
		
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// cau hinh phan quyen
		http.csrf().disable()
		.antMatcher("/admin/**") // gap link bat dau bang /admin thi kiem tra quyen
		.authorizeRequests()
		.antMatchers("/admin/login**") // gap link nay chi cho ADMIN va MANAGER truy cap
		.permitAll()
		.antMatchers("/admin/**") // gap link nay chi cho ADMIN va MANAGER truy cap
		.hasAnyRole("ADMIN", "MANAGER")
		.anyRequest().permitAll();
		
		// cau hinh dang nhap
		http.formLogin()
		.loginPage("/admin/login") // mac dinh la /login
		.loginProcessingUrl("/admin/login")
		.usernameParameter("email")
		.passwordParameter("password")
		.defaultSuccessUrl("/admin/category")
		.failureUrl("/admin/login?error=true");
		
		// cau hinh logout
		http.logout()
		.logoutUrl("/admin/logout")
		.logoutSuccessUrl("/admin/login")
		.deleteCookies("JSESSIONID");
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		// TODO Auto-generated method stub
		super.configure(web);
	}
}
