package com.myclass.sercurity;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import io.jsonwebtoken.Jwts;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter{

	private UserDetailsService _userDetailsService;
	public JWTAuthorizationFilter(AuthenticationManager authenticationManager, UserDetailsService userDetailsService) {
		super(authenticationManager);
		this._userDetailsService = userDetailsService;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
//		super.doFilterInternal(request, response, chain);
		
		//Lay value cua header voi key Authorization
		String headerAuthorization = request.getHeader("Authorization");
		System.out.println(headerAuthorization);
		
		//Kiem tra xem token co dc dinh kem hay ko
		if(headerAuthorization != null && !headerAuthorization.isEmpty()) {
			//lay ra chuoi token
			String token = headerAuthorization.replace("Bearer ", "");
			System.out.println("token: " + token);
			
			//giai ma token => lay email da gan vao trong subject khi tao token
			String email = Jwts.parser()
					.setSigningKey("ke_huy_diet")
					.parseClaimsJws(token)
					.getBody()
					.getSubject();
			
			// lay ra thong tin nguoi dung tu db theo email
			UserDetails userDetails = _userDetailsService.loadUserByUsername(email);
			
			// set thong tin user vao Security Context
			Authentication authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(authenticationToken);
		}
		chain.doFilter(request, response);
	}
}
