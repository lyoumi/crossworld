package com.crossworld.web.security;

import com.crossworld.web.security.AuthHeaders;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    public static AuthHeaders getAuthHeaders() {
        return (AuthHeaders) SecurityContextHolder.getContext().getAuthentication().getCredentials();
    }
}
