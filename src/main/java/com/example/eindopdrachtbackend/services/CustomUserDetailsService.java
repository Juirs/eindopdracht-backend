package com.example.eindopdrachtbackend.services;

import com.example.eindopdrachtbackend.models.Role;
import com.example.eindopdrachtbackend.models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getUserForAuthentication(username);

        String password = user.getPassword();
        Set<Role> roles = user.getRoles();

        List<GrantedAuthority> grantedRoles = new ArrayList<>();
        for (Role role : roles) {
            grantedRoles.add(new SimpleGrantedAuthority("ROLE_" + role.getRole()));
        }

        return new org.springframework.security.core.userdetails.User(
                username
                ,password
                ,user.getEnabled()
                ,true
                ,true
                ,true
                ,grantedRoles);
    }
}
