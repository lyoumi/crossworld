package com.crossworld.web.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityUtils {

    public static UserDetails getCurrentUser() {
        return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public static AuthHeaders getAuthHeaders() {
        return (AuthHeaders) SecurityContextHolder.getContext().getAuthentication().getCredentials();
    }
}
