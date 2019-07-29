package com.crossworld.auth.handlers.impl;

import com.crossworld.auth.data.CWAuthority;
import com.crossworld.auth.data.CWRole;
import com.crossworld.auth.data.CWUser;
import com.crossworld.auth.errors.exceptions.UserNotFound;
import com.crossworld.auth.handlers.UserRequestHandler;
import com.crossworld.auth.repositories.PermissionRepository;
import com.crossworld.auth.repositories.RoleRepository;
import com.crossworld.auth.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class UserRequestHandlerImpl implements UserRequestHandler {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Override
    public Mono<CWUser> getUserByName(String username) {
        return Mono.just(userRepository.getByUsername(username));
    }

    @Override
    public Mono<CWUser> createUser(CWUser cwUser) {
        return Mono.just(userRepository.saveAndFlush(cwUser));
    }

    @Override
    public Mono<CWUser> updateUser(CWUser cwUser) {
        return Mono.just(userRepository.saveAndFlush(cwUser));
    }

    @Override
    public Mono<Void> deleteUserById(String id) {
        return Mono.fromRunnable(() -> userRepository.deleteById(id));
    }

    @Override
    public Flux<CWUser> findAllUsers() {
        return Flux.fromIterable(userRepository.findAll());
    }

    @Override
    public Mono<CWUser> disableUser(String id) {
        return Mono.fromCallable(() -> userRepository.getOne(id))
                .switchIfEmpty(Mono.error(new UserNotFound(String.format("User with id %s not found", id))))
                .doOnSuccess(user -> user.setEnabled(false));
    }

    @Override
    public Mono<CWRole> createRole(CWRole role) {
        return Mono.just(roleRepository.saveAndFlush(role));
    }

    @Override
    public Mono<CWRole> updateRole(CWRole role) {
        return Mono.just(roleRepository.saveAndFlush(role));
    }

    @Override
    public Mono<CWRole> getRoleById(String id) {
        return Mono.just(roleRepository.getOne(id));
    }

    @Override
    public Mono<Void> deleteRoleById(String id) {
        return Mono.fromRunnable(() -> roleRepository.deleteById(id));
    }

    @Override
    public Mono<CWAuthority> createAuthority(CWAuthority authority) {
        return Mono.just(permissionRepository.saveAndFlush(authority));
    }

    @Override
    public Mono<Void> deleteAuthorityById(String id) {
        return Mono.fromRunnable(() -> permissionRepository.deleteById(id));
    }
}
