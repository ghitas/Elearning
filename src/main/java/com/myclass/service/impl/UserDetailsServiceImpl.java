package com.myclass.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.myclass.dto.CustomUserDetails;
import com.myclass.entity.User;
import com.myclass.repository.UserRepository;

@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService{

	@Autowired
	private UserRepository userRepository;
	
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		// lay ra thong tin user tu database
		User user = userRepository.findByEmail(email);
		if(user == null) throw new UsernameNotFoundException("Khong tim thay thong tin");
		
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		String roleName = user.getRole().getName();
		authorities.add(new SimpleGrantedAuthority(roleName));
		CustomUserDetails userDetails =  new CustomUserDetails(user.getEmail(), user.getPassword(), authorities);
		userDetails.setFullName(user.getFullname());
		return userDetails;
	}

}
