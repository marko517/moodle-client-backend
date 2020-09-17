package com.moodle.application.service;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.moodle.application.model.User;
import com.moodle.application.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService{

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username).orElseThrow(
				() -> new UsernameNotFoundException("Username not found: " + username));
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), 
				true, true, true, true, getAuthorities("ROLE_USER"));
	}

	private Collection<? extends GrantedAuthority> getAuthorities(String roleUser) {
		return Collections.singletonList(new SimpleGrantedAuthority(roleUser));
	}
}
