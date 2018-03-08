package com.brianchang.web.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import com.brianchang.web.services.UserService;

@Component
@Lazy
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler{
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private UserService service;
	
	public CustomLogoutSuccessHandler(UserService service) {
		this.service = service;
	}
	
	@Override
	public void onLogoutSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		// Code For Business Here
		service.findByUpdatedAt(service.findByEmail(authentication.getName()));
		
		
		logger.info("Logout Successful with Principal: " + authentication.getName());
		
		response.setStatus(HttpServletResponse.SC_OK);
        //redirect to login
		response.sendRedirect("/login");
	}

}
