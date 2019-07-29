package com.crossworld.auth.handlers;

import com.crossworld.auth.data.CWAuthority;
import com.crossworld.auth.data.CWRole;
import com.crossworld.auth.data.CWUser;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRequestHandler {

    Mono<CWUser> getUserByName(String username);

    Mono<CWUser> createUser(CWUser cwUser);

    Mono<CWUser> updateUser(CWUser cwUser);

    Mono<Void> deleteUserById(String id);

    Flux<CWUser> findAllUsers();

    Mono<CWUser> disableUser(String id);

    Mono<CWRole> createRole(CWRole role);

    Mono<CWRole> updateRole(CWRole role);

    Mono<CWRole> getRoleById(String id);

    Mono<Void> deleteRoleById(String id);

    Mono<CWAuthority> createAuthority(CWAuthority authority);

    Mono<Void> deleteAuthorityById(String id);
}
