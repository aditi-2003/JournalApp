package com.aditi.journal_app.service;

import com.aditi.journal_app.model.User;
import com.aditi.journal_app.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // Wrap your custom User entity into Spring Security's UserDetails
        UserDetails ud=  org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword()) // already encoded
                .authorities(user.getRoles().toArray(new String[0])) // roles converted to authorities
                .build();
        System.out.println("Authorities: " + ud.getAuthorities());
        return ud;
    }
}
