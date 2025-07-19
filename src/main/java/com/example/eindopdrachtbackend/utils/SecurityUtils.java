package com.example.eindopdrachtbackend.utils;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

    public static boolean isOwnerOrAdmin(UserDetails currentUser, String resourceOwnerUsername) {
        return currentUser.getUsername().equals(resourceOwnerUsername) ||
               hasAdminRole(currentUser);
    }

    public static boolean hasAdminRole(UserDetails currentUser) {
        return currentUser.getAuthorities().stream()
               .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
    }

    public static boolean hasRole(UserDetails currentUser, String roleName) {
        return currentUser.getAuthorities().stream()
               .anyMatch(auth -> auth.getAuthority().equals("ROLE_" + roleName));
    }

    public static boolean hasAnyRole(UserDetails currentUser, String... roleNames) {
        for (String roleName : roleNames) {
            if (hasRole(currentUser, roleName)) {
                return true;
            }
        }
        return false;
    }
}
