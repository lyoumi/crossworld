package com.crossworld.auth.repositories;

import com.crossworld.auth.data.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, String> {
}
