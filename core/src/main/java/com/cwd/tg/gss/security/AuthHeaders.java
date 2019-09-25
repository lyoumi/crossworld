package com.cwd.tg.gss.security;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthHeaders {
    private String requestId;
    private String authToken;
}
