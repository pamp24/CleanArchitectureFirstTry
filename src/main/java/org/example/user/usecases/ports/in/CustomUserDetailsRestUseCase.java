package org.example.user.usecases.ports.in;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface CustomUserDetailsRestUseCase {

	UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException;

	UserDetails loadUserById(Long id);

}