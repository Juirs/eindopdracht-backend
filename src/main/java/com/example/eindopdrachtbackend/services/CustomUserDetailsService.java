package com.example.eindopdrachtbackend.services;

import com.example.eindopdrachtbackend.dtos.UserResponseDto;
import com.example.eindopdrachtbackend.models.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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
    public UserDetails loadUserByUsername(String username) {
        UserResponseDto userResponseDto = userService.getUser(username);

        String password = userResponseDto.getPassword();
        Set<Role> roles = userResponseDto.getRoles();

        List<GrantedAuthority> grantedRoles = new ArrayList<>();
        for (Role role: roles) {
            grantedRoles.add(new SimpleGrantedAuthority(role.getRole()));
        }

        return new org.springframework.security.core.userdetails.User(username, password, grantedRoles);
    }
}
