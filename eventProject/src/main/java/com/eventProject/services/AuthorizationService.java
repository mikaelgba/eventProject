package com.eventProject.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface AuthorizationService {

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

}
