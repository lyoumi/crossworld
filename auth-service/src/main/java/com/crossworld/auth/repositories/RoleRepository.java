package com.crossworld.auth.repositories;

import com.crossworld.auth.data.CWRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<CWRole, String> {
}
