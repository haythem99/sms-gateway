package com.grvnc.notification.security.aouth2;

 
import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.grvnc.notification.integration.clientof.util.UtilsServiceIntegrator;
import com.grvnc.sharedlib.outh2.UtilOuth2;

import lombok.extern.slf4j.Slf4j;
 
@Slf4j
@Component
public class CustomeOuth2AuthrizationFilter extends OncePerRequestFilter{
	 @Autowired
	 private UtilsServiceIntegrator utilsServiceMS;
	 
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		try {
//			String clientPasscodes=utilsServiceMS.getConfigValueOf("security.accepted.client-passcodes");
			UtilOuth2.authrization(request, response, filterChain);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
			e.printStackTrace();
			throw new ServletException(e.getMessage(), e);
		}
		
	}
}
