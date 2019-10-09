package com.myclass.api.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.myclass.dto.UserLogin;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
public class ApiLoginController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@PostMapping("api/login")
	public ResponseEntity<String> login(@RequestBody UserLogin userLogin){
		//b1: thuc hien dang nhap
		Authentication authenticationToken = new UsernamePasswordAuthenticationToken(userLogin.getEmail(), userLogin.getPassword());
		try {
			Authentication authentication = authenticationManager.authenticate(authenticationToken);
			//b2 dang nhap thanh cong -> luu ket qua tra ve trong security context
			SecurityContextHolder.getContext().setAuthentication(authentication);
			//tao chuoi token tra ve cho client
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			Date now = new Date();
			String token = Jwts.builder()
					.setSubject(userDetails.getUsername())
					.setIssuedAt(now)
					.setExpiration(new Date(now.getTime() + 864000000L)) // thoi gian ton tai cua token
					.signWith(SignatureAlgorithm.HS512, "ke_huy_diet")
					.compact();
			return new ResponseEntity<String>(token, HttpStatus.OK);
			
		} catch (AuthenticationException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<String>("Sai ten dang nhap hoac mat khau", HttpStatus.BAD_REQUEST);
	}
}
