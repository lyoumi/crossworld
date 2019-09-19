//package com.crossworld.web.security;
//
//import com.crossworld.web.clients.AuthWebClient;
//import lombok.AllArgsConstructor;
//import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//import reactor.core.publisher.Mono;
//
//import java.util.UUID;
//
//@Component
//@AllArgsConstructor
//public class UserDetailsServiceImpl implements ReactiveUserDetailsService {
//
//    private final AuthWebClient authWebClient;
//
//    @Override
//    public Mono<UserDetails> findByUsername(String username) {
//        return authWebClient.validateUserToken(username, UUID.randomUUID().toString()).cast(UserDetails.class);
//    }
//}
