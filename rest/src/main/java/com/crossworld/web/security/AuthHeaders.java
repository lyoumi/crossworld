package com.crossworld.web.security;

import lombok.Data;

@Data
public class AuthHeaders {
    private String requestId;
    private String authToken;
}
