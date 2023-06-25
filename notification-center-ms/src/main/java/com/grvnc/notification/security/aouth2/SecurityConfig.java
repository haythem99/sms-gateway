package com.grvnc.notification.security.aouth2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;


 

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
		  prePostEnabled = true, 
		  securedEnabled = true, 
		  jsr250Enabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter{

 @Autowired
 private CustomeOuth2AuthrizationFilter customeOuth2AuthrizationFilter;


	@Override
	protected void configure(HttpSecurity http) throws Exception {
	
		http.csrf().disable();
		http.cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues());
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.authorizeRequests().antMatchers("/public/**").permitAll(); //access wihthout aoth
		http.authorizeRequests().antMatchers("/error/**").permitAll();  
//		swagger
		http.authorizeRequests().antMatchers("/swagger-ui/**").permitAll();
//		swagger
		http.authorizeRequests().antMatchers("/v3/api-docs/**").permitAll();
		http.authorizeRequests().anyRequest().authenticated();
		http.addFilterBefore(customeOuth2AuthrizationFilter, BasicAuthenticationFilter.class);
	}

 
 
 
}