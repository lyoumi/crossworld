package com.crossworld.auth.repositories;

import com.crossworld.auth.data.CWUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<CWUser, String> {
    CWUser getByUsername(String username);
}
