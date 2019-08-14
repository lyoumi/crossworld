package com.crossworld.auth.repositories;

import com.crossworld.auth.data.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Authority, String> {
}
