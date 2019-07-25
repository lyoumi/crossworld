package com.crossworld.auth.repositories;

import com.crossworld.auth.data.CWAuthority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<CWAuthority, String> {
}
